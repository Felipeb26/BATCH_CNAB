package com.batsworks.batch.service;

import com.batsworks.batch.config.utils.BatchParameters;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CnabService {

    private final ArquivoRepository arquivoRepository;
    private final JobLauncher asyncWrite;
    private final Job jobWriteCnab;
    private final BatchParameters parameters;
    @Value("${configuration.default_folder:tmp}")
    private String path;


    public DefaultMessage uploadCnabFile(MultipartFile file, CnabType tipo) {
        try {
            var fileName = StringUtils.cleanPath(isNull(file.getOriginalFilename()) ? file.getOriginalFilename() : randomName());
            var storagePlace = Paths.get(path);
            var targetLocation = storagePlace.resolve(fileName);
            file.transferTo(targetLocation);

            var data = compressData(file.getBytes());
            var arquivo = Arquivo.builder()
                    .name(fileName)
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
            return new DefaultMessage(e.getMessage(), Status.PROCESSADO_ERRO);
        }
    }

    public DefaultMessage downloadCnab() {
        try {
            var jobParameters = new JobParametersBuilder()
                    .addJobParameter("download_cnab", randomName(), String.class, false)
                    .toJobParameters();
            asyncWrite.run(jobWriteCnab, jobParameters);
            return new DefaultMessage("Download", Status.DOWNLOADING);
        } catch (Exception e) {
            log.error("log: {}", e.getMessage());
            return new DefaultMessage(e.getMessage(), Status.DOWNLOAD_ERROR);
        }
    }

    public String string() {
        var arquivo = arquivoRepository.findById(1L).orElse(null);
        if (arquivo == null) return "dnjksçbdvfikloj";
        return arquivo.getName();
    }
}
