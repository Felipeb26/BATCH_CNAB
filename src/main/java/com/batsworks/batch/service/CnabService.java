package com.batsworks.batch.service;

import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.CnabType;
import com.batsworks.batch.domain.enums.Status;
import com.batsworks.batch.domain.records.DefaultMessage;
import com.batsworks.batch.repository.ArquivoRepository;
import com.batsworks.batch.utils.Files;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.Optional;

import static com.batsworks.batch.utils.Files.*;
import static com.batsworks.batch.utils.Formats.actualDateString;
import static com.batsworks.batch.utils.Formats.mask;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class CnabService {

    private final ArquivoRepository arquivoRepository;
    private final JobLauncher asyncWrite;
    private final Job jobWriteCnab;
    @Value("${configuration.default_folder:tmp}")
    private String tempFolderPath;


    public DefaultMessage uploadCnabFile(MultipartFile file, CnabType tipo) {
        Arquivo arquivo = new Arquivo();
        try {
            var fileName = StringUtils.cleanPath(nonNull(file.getOriginalFilename()) ? file.getOriginalFilename() : randomFileName());
            var data = compressData(file.getBytes(), fileName);

            arquivo = Arquivo.builder()
                    .name(fileName)
                    .extension(fileType(file.getInputStream(), fileName))
                    .fileSize(String.valueOf(file.getSize()))
                    .situacao(Status.PROCESSANDO)
                    .file(data)
                    .build();


            arquivo = arquivoRepository.save(arquivo);

            var storagePlace = Paths.get(tempFolderPath).resolve(mask(fileName, arquivo.getId()));
            var haveSaved = transferFile(file.getInputStream(), storagePlace);

            if (Boolean.FALSE.equals(haveSaved))
                throw new BussinesException(BAD_REQUEST, "Erro ao salvar para analise posterior do arquivo %s ".formatted(fileName), new Object[]{Status.PROCESSANDO});
            return new DefaultMessage("Analisando arquivo %s ".formatted(fileName), Status.PROCESSANDO);
        } catch (Exception e) {
            log.error(e.getMessage());
            arquivo.setSituacao(Status.PROCESSADO_ERRO);
            arquivo.setObservacao(e.getMessage());
            arquivoRepository.save(arquivo);
            throw new BussinesException(BAD_REQUEST, e.getMessage(), new Object[]{Status.PROCESSADO_ERRO});
        }
    }

    public byte[] downloadCnab(Boolean retorno, Long idArquivo) {
        try {

            Optional<byte[]> optionalString = arquivoRepository.findArquivoById(idArquivo);
            if (optionalString.isEmpty())
                throw new BussinesException(BAD_REQUEST, "Arquivo não encontrado", new Object[]{Status.DOWNLOAD_ERROR});

            var arquivo = optionalString.get();

            var jobParameters = new JobParametersBuilder()
                    .addJobParameter("download_cnab", randomFileName(), String.class, false)
                    .addJobParameter("TIME", actualDateString(), String.class)
                    .addJobParameter("id", idArquivo, Long.class)
                    .toJobParameters();
            asyncWrite.run(jobWriteCnab, jobParameters);
//            var data = decompressData(arquivo);
//            if (data.length == 0) {
//                throw new BussinesException(BAD_REQUEST, "An error has happen while unzipping the file!", new Object[]{Status.DOWNLOAD_ERROR});
//            }
            return new byte[0];
        } catch (Exception e) {
            log.error("log: {}", e.getMessage());
            throw new BussinesException(BAD_REQUEST, e.getMessage(), new Object[]{Status.DOWNLOAD_ERROR});
        }
    }

    public String resetTempFile() {
        try {
            Files.deleteFile(tempFolderPath);
            return "Pasta tmp deletada com sucesso";
        } catch (Exception e) {
            throw new BussinesException(BAD_REQUEST, "erro ao resetar pasta tmp ", new Object[]{Status.ERROR});
        }
    }

    public Object string(Long id) {
        var arquivo = arquivoRepository.findById(id).orElse(null);
        if (arquivo == null) return "dnjksçbdvfikloj";
        return decompressData(arquivo.getFile());
    }
}
