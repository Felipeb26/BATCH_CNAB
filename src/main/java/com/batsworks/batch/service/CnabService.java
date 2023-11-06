package com.batsworks.batch.service;

import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.config.utils.BatchParameters;
import com.batsworks.batch.config.utils.Compress;
import com.batsworks.batch.config.utils.Utilities;
import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.CnabType;
import com.batsworks.batch.domain.enums.Status;
import com.batsworks.batch.domain.records.DefaultMessage;
import com.batsworks.batch.repository.ArquivoRepository;
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
import java.util.HashMap;
import java.util.Map;

import static com.batsworks.batch.config.utils.Utilities.*;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class CnabService {

    private final Compress compress;
    private final ArquivoRepository arquivoRepository;
    private final JobLauncher asyncWrite;
    private final Job jobWriteCnab;
    private final BatchParameters parameters;
    @Value("${configuration.default_folder:tmp}")
    private String tempFolderPath;


    public DefaultMessage uploadCnabFile(MultipartFile file, CnabType tipo) {
        try {
            var fileName = StringUtils.cleanPath(isNull(file.getOriginalFilename()) ? file.getOriginalFilename() : randomFileName());
            var storagePlace = Paths.get(tempFolderPath);
            var haveSaved = transferFile(file.getInputStream(), storagePlace.resolve(fileName));
            if (Boolean.FALSE.equals(haveSaved))
                throw new BussinesException(BAD_REQUEST, "Erro ao analisar arquivo %s ".formatted(fileName), new Object[]{Status.PROCESSANDO});

            var data = compressData(file.getBytes(), fileName);
            var arquivo = Arquivo.builder()
                    .name(fileName + "\t" + data.length)
                    .extension(fileType(file.getInputStream(), fileName))
                    .fileSize(String.valueOf(file.getSize()))
                    .situacao(Status.PROCESSANDO)
                    .file(encodeByteToBASE64String(data))
                    .build();


            arquivo = arquivoRepository.save(arquivo);

            Map<String, Object> map = new HashMap<>();
            map.put("id", arquivo.getId());

            parameters.setParameters(map);
            return new DefaultMessage("Analisando arquivo %s ".formatted(fileName), Status.PROCESSANDO);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BussinesException(BAD_REQUEST, e.getMessage(), new Object[]{Status.PROCESSADO_ERRO});
        }
    }

    public byte[] downloadCnab(Boolean retorno, Long idArquivo) {
        try {
            var jobParameters = new JobParametersBuilder()
                    .addJobParameter("download_cnab", randomFileName(), String.class, false)
                    .toJobParameters();
            asyncWrite.run(jobWriteCnab, jobParameters);
            return null;
        } catch (Exception e) {
            log.error("log: {}", e.getMessage());
            throw new BussinesException(BAD_REQUEST, e.getMessage(), new Object[]{Status.DOWNLOAD_ERROR});
        }
    }

    public String resetTempFile() {
        try {
            Utilities.deleteFile(tempFolderPath);
            return "Pasta tmp deletada com sucesso";
        } catch (Exception e) {
            throw new BussinesException(BAD_REQUEST, "erro ao resetar pasta tmp ", new Object[]{Status.ERROR});
        }
    }

    public String string() {
        var arquivo = arquivoRepository.findById(1L).orElse(null);
        if (arquivo == null) return "dnjksçbdvfikloj";
        return arquivo.getName();
    }
}
