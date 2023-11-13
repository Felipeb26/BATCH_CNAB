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

import static com.batsworks.batch.utils.UtilitiesFiles.findFile;
import static com.batsworks.batch.utils.UtilitiesFiles.resolveFileName;
import static com.batsworks.batch.utils.UtilitiesParse.actualDateString;

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
            String file = findFile();
            if (file == null) return;

            Long id = Long.valueOf(resolveFileName(file, true));

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
}
