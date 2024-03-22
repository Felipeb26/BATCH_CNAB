package com.batsworks.batch.service.impl;

import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.config.exception.StatusEnum;
import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.CnabStatus;
import com.batsworks.batch.domain.enums.FileType;
import com.batsworks.batch.domain.mapper.ArquivoMapper;
import com.batsworks.batch.domain.records.ArquivoDTO;
import com.batsworks.batch.domain.records.DefaultMessage;
import com.batsworks.batch.domain.records.PageDTO;
import com.batsworks.batch.repository.ArquivoRepository;
import com.batsworks.batch.repository.BoletoAlteracaoRepository;
import com.batsworks.batch.repository.CnabErroRepository;
import com.batsworks.batch.repository.CnabRepository;
import com.batsworks.batch.service.ArquivoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.batsworks.batch.utils.Files.*;
import static com.batsworks.batch.utils.Formats.actualDateString;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArquivoServiceImpl implements ArquivoService {
    private final BoletoAlteracaoRepository boletoAlteracaoRepository;

    private final ArquivoRepository arquivoRepository;
    private final JobLauncher asyncWrite;
    private final Job jobWriteCnab;
    private final AmqpTemplate rabbitTemplate;
    private final ArquivoMapper arquivoMapper;
    private final CnabRepository cnabRepository;
    private final CnabErroRepository cnabErroRepository;

    @Override
    public DefaultMessage uploadCnabFile(MultipartFile file, FileType tipo, String observcao) {
        Arquivo arquivo = new Arquivo();
        try {
            if (tipo.equals(FileType.CNAB400)) {
                var fileName = StringUtils.cleanPath(nonNull(file.getOriginalFilename()) ? file.getOriginalFilename() : randomFileName());
                arquivo = Arquivo.builder().nome(fileName)
                        .extension(fileType(file.getInputStream(), fileName))
                        .fileSize(file.getSize())
                        .file(file.getBytes())
                        .observacao(observcao)
                        .situacao(CnabStatus.PROCESSANDO).build();

                if (Boolean.FALSE.equals(validFile(file, fileName)))
                    throw new BussinesException(BAD_REQUEST, "Arquivo Invalido");

                var data = compressData(file.getBytes());
                arquivo.setFile(data);
                arquivo = arquivoRepository.save(arquivo);

                rabbitTemplate.convertAndSend("arquivo.cnab", arquivo);
            }
            return new DefaultMessage("Analisando arquivo %s ".formatted(arquivo.getNome()), CnabStatus.PROCESSANDO);
        } catch (Exception e) {
            log.error(e.getMessage());
            arquivo.setSituacao(CnabStatus.PROCESSADO_ERRO);
            arquivo.setObservacao(e.getMessage());
            arquivoRepository.save(arquivo);
            throw new BussinesException(BAD_REQUEST, StatusEnum.ARQUVIVO_PROCESSADO_ERRO, e.getMessage());
        }
    }

    @Override
    public byte[] downloadCnab(Boolean retorno, Long idArquivo) {
        try {
            if (Boolean.FALSE.equals(retorno)) {
                byte[] arquivo = arquivoRepository.findArquivoById(idArquivo).orElseThrow(() ->
                        new BussinesException(BAD_REQUEST, "Arquivo não encontrado"));
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
            throw new BussinesException(BAD_REQUEST, StatusEnum.ARQUVIVO_DOWNLOAD_ERROR, e.getMessage());
        }
    }

    @Override
    public PageDTO<Arquivo, ArquivoDTO> findAllByPage(Pageable pageable) {
        var page = arquivoRepository.findAll(pageable);
        if (page.isEmpty())
            throw new BussinesException(BAD_REQUEST, StatusEnum.ERROR_WITH_MESSAGE, "Nenhum Arquivo encontrado");

        return new PageDTO<>(page, arquivoMapper::toDTO);
    }

    @Override
    public ArquivoDTO findArquivoByID(Long id) {
        Arquivo arquivo = arquivoRepository.findById(id).orElseThrow(() -> new BussinesException(BAD_REQUEST, "File not Found with id %s".formatted(id)));
        return arquivoMapper.toDTO(arquivo);
    }

    @Transactional
    @Override
    public void deleteArquivo(Long id) {
        try {
            boletoAlteracaoRepository.deleteAllByIdArquivo(id);
            cnabErroRepository.deleteAllByIdArquivo(id);
            cnabRepository.deleteAllByIdArquivo(id);
            arquivoRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BussinesException(BAD_REQUEST, StatusEnum.ERROR_WITH_MESSAGE, e.getMessage());
        }
    }

    @Override
    @Cacheable("EntityCache")
    public Arquivo findArquivoEntityById(Long id) {
        return arquivoRepository.findById(id).orElseThrow(() -> new BussinesException(BAD_REQUEST, "File not Found with id %s".formatted(id)));
    }
}
