package com.batsworks.batch.service;

import static com.batsworks.batch.config.utils.Utilities.*;
import static java.util.Objects.*;
import static org.springframework.http.HttpStatus.*;

import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.config.utils.AsyncFunctions;
import com.batsworks.batch.config.utils.Compress;
import com.batsworks.batch.config.utils.Utilities;
import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.CnabType;
import com.batsworks.batch.domain.enums.Status;
import com.batsworks.batch.domain.records.DefaultMessage;
import com.batsworks.batch.repository.ArquivoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CnabService {

    private final Compress compress;
    private final ArquivoRepository arquivoRepository;
    private final JobLauncher asyncWrite, jobLauncherAsync;
    private final Job jobWriteCnab, jobCnab;
    @Value("${configuration.default_folder:tmp}")
    private String tempFolderPath;


    public DefaultMessage uploadCnabFile(MultipartFile file, CnabType tipo) {
        Arquivo arquivo = new Arquivo();
        try {
            var fileName = StringUtils.cleanPath(isNull(file.getOriginalFilename()) ? file.getOriginalFilename() : randomFileName());
            var data = compressData(file.getBytes(), fileName);

            arquivo = Arquivo.builder()
                    .name(fileName)
                    .extension(fileType(file.getInputStream(), fileName))
                    .fileSize(String.valueOf(file.getSize()))
                    .situacao(Status.PROCESSANDO)
                    .file(encodeByteToBASE64String(data))
                    .build();


            arquivo = arquivoRepository.save(arquivo);

            var storagePlace = Paths.get(tempFolderPath).resolve(mask(fileName, arquivo.getId()));
            var haveSaved = transferFile(file.getInputStream(), storagePlace);

            if (Boolean.FALSE.equals(haveSaved))
                throw new BussinesException(BAD_REQUEST, "Erro ao analisar arquivo %s ".formatted(fileName), new Object[]{Status.PROCESSANDO});


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
            AsyncFunctions<byte[], byte[]> asyncFunctions = new AsyncFunctions<>();

            Optional<String> optionalString = arquivoRepository.findArquivoById(idArquivo);
            if (optionalString.isEmpty())
                throw new BussinesException(BAD_REQUEST, "Arquivo não encontrado", new Object[]{Status.DOWNLOAD_ERROR});

            var arquivo = optionalString.get();
            var bytes = Utilities.decodeBASE64(arquivo.getBytes());
//            var descompressData = asyncFunctions.object(Compress::decompressData, bytes);

//            var jobParameters = new JobParametersBuilder()
//                    .addJobParameter("download_cnab", randomFileName(), String.class, false)
//                    .toJobParameters();
//            asyncWrite.run(jobWriteCnab, jobParameters);
//            var data = descompressData.get();
            var data = compress.decompressData(bytes);
            if (data.length == 0) {
                throw new BussinesException(BAD_REQUEST, "An error has happen while unzipping the file!", new Object[]{Status.DOWNLOAD_ERROR});
            }
            return data;
//            return descompressData.get();
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
