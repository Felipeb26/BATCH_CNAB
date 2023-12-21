package com.batsworks.batch.config.quartz;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.nio.charset.StandardCharsets;

import static com.batsworks.batch.utils.Files.findFileRem;
import static com.batsworks.batch.utils.Files.resolveFileName;
import static com.batsworks.batch.utils.Formats.actualDateString;
import static com.batsworks.batch.utils.Formats.decodeBASE64;

@Getter
@Setter
@Slf4j
public class QuartzJob extends QuartzJobBean {

    private String jobName;
    private JobLauncher jobLauncher;
    private JobLocator jobLocator;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            String file = findFileRem();
            if (file == null) return;

            String decodedFileName = file;
            decodedFileName = formatFileName(decodedFileName);

            log.info("DECODED {}", decodedFileName);
            decodedFileName = new String(decodeBASE64(decodedFileName.getBytes(StandardCharsets.UTF_8)));
            Long id = Long.valueOf(resolveFileName(decodedFileName, true));

            Job job = jobLocator.getJob(jobName);
            JobParameters parameters = new JobParametersBuilder()
                    .addString("time", actualDateString())
                    .addJobParameter("path", file, String.class, true)
                    .addJobParameter("id", id, Long.class, true)
                    .toJobParameters();

            jobLauncher.run(job, parameters);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private String formatFileName(String name) {
        name = name.replace(".rem", "");
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
