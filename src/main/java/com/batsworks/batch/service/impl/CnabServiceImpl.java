package com.batsworks.batch.service.impl;

import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.CnabType;
import com.batsworks.batch.domain.enums.Status;
import com.batsworks.batch.domain.records.DefaultMessage;
import com.batsworks.batch.repository.ArquivoRepository;
import com.batsworks.batch.service.CnabService;
import com.batsworks.batch.utils.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.batsworks.batch.utils.Files.*;
import static com.batsworks.batch.utils.Formats.actualDateString;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class CnabServiceImpl implements CnabService {

    private final ArquivoRepository arquivoRepository;
    private final JobLauncher asyncWrite;
    private final Job jobWriteCnab;
    private final AmqpTemplate rabbitTemplate;
    @Value("${configuration.default_folder:tmp}")
    private String tempFolderPath;

    @Override
    public DefaultMessage uploadCnabFile(MultipartFile file, CnabType tipo) {
        Arquivo arquivo = new Arquivo();
        try {
            var fileName = StringUtils.cleanPath(nonNull(file.getOriginalFilename()) ? file.getOriginalFilename() : randomFileName());
            arquivo = Arquivo.builder().nome(fileName)
                    .extension(fileType(file.getInputStream(), fileName))
                    .fileSize(file.getSize())
                    .file(file.getBytes())
                    .situacao(Status.PROCESSANDO).build();

            if (!validFile(file, fileName)) throw new BussinesException(BAD_REQUEST, "Arquivo Invalido");

            var data = compressData(file.getBytes(), arquivo.getNome());
            arquivo.setFile(data);
            arquivo = arquivoRepository.save(arquivo);

            rabbitTemplate.convertAndSend("arquivo.cnab", arquivo);
            return new DefaultMessage("Analisando arquivo %s ".formatted(arquivo.getNome()), Status.PROCESSANDO);
        } catch (Exception e) {
            log.error(e.getMessage());
            arquivo.setSituacao(Status.PROCESSADO_ERRO);
            arquivo.setObservacao(e.getMessage());
            arquivoRepository.save(arquivo);
            throw new BussinesException(BAD_REQUEST, e.getMessage(), new Object[]{Status.PROCESSADO_ERRO});
        }
    }

    @Override
    public byte[] downloadCnab(Boolean retorno, Long idArquivo) {
        try {
            if (!retorno) {
                byte[] arquivo = arquivoRepository.findArquivoById(idArquivo).orElseThrow(() ->
                        new BussinesException(BAD_REQUEST, "Arquivo não encontrado", new Object[]{Status.DOWNLOAD_ERROR}));
                return decompressData(arquivo);
            }

            var jobParameters = new JobParametersBuilder()
                    .addJobParameter("download_cnab", randomFileName(), String.class, false)
                    .addJobParameter("TIME", actualDateString(), String.class)
                    .addJobParameter("id", idArquivo, Long.class)
                    .toJobParameters();
            asyncWrite.run(jobWriteCnab, jobParameters);

            return new byte[0];
        } catch (Exception e) {
            log.error("log: {}", e.getMessage());
            throw new BussinesException(BAD_REQUEST, e.getMessage(), new Object[]{Status.DOWNLOAD_ERROR});
        }
    }

    @Override
    @Cacheable("Arquivo")
    public Arquivo findArquivoByID(Long id) {
        return arquivoRepository.findById(id).orElseThrow(() -> new BussinesException(BAD_REQUEST, "File not Found with id %s".formatted(id)));
    }

    @Override
    public String resetTempFile() {
        try {
            Files.deleteFile(tempFolderPath);
            return "Pasta tmp deletada com sucesso";
        } catch (Exception e) {
            throw new BussinesException(BAD_REQUEST, "erro ao resetar pasta tmp ", new Object[]{Status.ERROR});
        }
    }
}
