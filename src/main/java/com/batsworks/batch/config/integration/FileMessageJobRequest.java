package com.batsworks.batch.config.integration;

import com.batsworks.batch.config.utils.BatchParameters;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;

@Setter
@Component
@RequiredArgsConstructor
public class FileMessageJobRequest {

    private final BatchParameters parameters;
    private JobParametersBuilder jobParameters;
    private String fileName = "path";
    private Job job;

    @Transformer
    public JobLaunchRequest jobLaunchRequest(Message<File> fileMessage) {
        jobParameters = new JobParametersBuilder();
        var parameter = parameters.getParameters();

        jobParameters.addJobParameter("id", (long) parameter.get("id"), Long.class);
        jobParameters.addJobParameter(fileName, fileMessage.getPayload().getAbsolutePath(), String.class);
        return new JobLaunchRequest(job, jobParameters.toJobParameters());
    }

}
