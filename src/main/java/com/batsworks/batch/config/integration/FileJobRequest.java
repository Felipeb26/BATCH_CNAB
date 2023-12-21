package com.batsworks.batch.config.integration;

import com.batsworks.batch.domain.enums.JobParamsEnum;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static com.batsworks.batch.utils.Files.resolveFileName;
import static com.batsworks.batch.utils.Formats.actualDateString;
import static com.batsworks.batch.utils.Formats.decodeBASE64;

@Slf4j
@Setter
@Component
public class FileJobRequest {

    private Job job;

    public JobLaunchRequest jobLaunchRequest(Message<File> fileMessage) {
        try {
            String decodedFileName = fileMessage.getPayload().getAbsolutePath();
            decodedFileName = formatFileName(decodedFileName);

            log.info("DECODED {}", decodedFileName);
            decodedFileName = new String(decodeBASE64(decodedFileName.getBytes(StandardCharsets.UTF_8)));

            String fileName = resolveFileName(decodedFileName, false);
            Long id = Long.valueOf(resolveFileName(decodedFileName, true));

            JobParameters parameters = new JobParametersBuilder()
                    .addString(JobParamsEnum.TIME.param(), actualDateString())
                    .addJobParameter(JobParamsEnum.PATH.param(), fileMessage.getPayload().getAbsolutePath(), String.class, true)
                    .addJobParameter(JobParamsEnum.ID.param(), id, Long.class, true)
                    .addJobParameter(JobParamsEnum.FILENAME.param(), fileName, String.class)
                    .toJobParameters();

            return new JobLaunchRequest(job, parameters);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    private String formatFileName(String name) {
        name = name.replace(".rem", "");
        name = name.replace(".processing", "");

        int lastIndex = name.lastIndexOf("\\");
        if (lastIndex <= 0) {
            lastIndex = name.lastIndexOf("/");
        }
        if (lastIndex > 0) {
            name = name.substring(lastIndex + 1);
        }

        int index = name.indexOf("@");
        if (index > 0) {
            name = name.substring(index + 1);
        }
        return name;
    }
}
