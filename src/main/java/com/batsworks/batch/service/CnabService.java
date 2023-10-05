package com.batsworks.batch.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
public class CnabService {

    private final Path pathStorage;
    private final JobLauncher jobLauncher;
    private final Job job;

    public CnabService(@Value("${file.upload-dir:tmp}") String uploadDir, @Qualifier("asyncJobLauncher") JobLauncher jobLauncher, Job job) {
        this.pathStorage = Paths.get(uploadDir);
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public void uploadCnabFile(MultipartFile file) throws Exception {
        var fileName = StringUtils.cleanPath(nonNull(file.getOriginalFilename()) ? file.getOriginalFilename() : randomName());
        var location = pathStorage.resolve(fileName);
        file.transferTo(location);

        var jobParameters = new JobParametersBuilder()
                .addJobParameter("cnab", fileName, String.class, true)
                .addJobParameter("cnabFile", "file:" + location, String.class)
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
    }

    private String randomName() {
        return UUID.randomUUID().toString();
    }
}
