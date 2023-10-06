package com.batsworks.batch.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CnabService {

    private final Path pathStorage;
    //    private final JobLauncher jobLauncher;
    private final JobLauncher cnab400Launcher;
    private final Job job;

    public CnabService(@Value("${file.upload-dir:tmp}") String uploadDir,
                       @Qualifier("jobLauncherAsync") JobLauncher cnab400Launcher,
                       Job job) {
        this.pathStorage = Paths.get(uploadDir);
        this.cnab400Launcher = cnab400Launcher;
//        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public void uploadCnabFile(MultipartFile file, String tipo) throws Exception {
        var fileName = StringUtils.cleanPath(file.getOriginalFilename() == null ? file.getOriginalFilename() : randomName());
        var location = pathStorage.resolve(fileName);
        file.transferTo(location);

        var jobParameters = new JobParametersBuilder()
                .addJobParameter("cnab", fileName, String.class, true)
                .addJobParameter("cnabFile", "file:" + location, String.class)
                .toJobParameters();

        if (tipo.equalsIgnoreCase("400"))
            cnab400Launcher.run(job, jobParameters);
//        else
//            jobLauncher.run(job, jobParameters);

        removeTempFile(fileName);
    }

    private String randomName() {
        return UUID.randomUUID() + ".rem";
    }

    private static void removeTempFile(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }

}
