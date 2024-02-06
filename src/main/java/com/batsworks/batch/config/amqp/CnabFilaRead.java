package com.batsworks.batch.config.amqp;

import com.batsworks.batch.domain.entity.Arquivo;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;

import static com.batsworks.batch.utils.Formats.actualDateString;

@Component
@RequiredArgsConstructor
public class CnabFilaRead {

    private final JobLauncher jobLauncher;
    private final Job jobCnab;

    @RabbitListener(queues = "arquivo.cnab")
    public void receveFile(Arquivo arquivo) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters parameters = new JobParametersBuilder()
                .addString("time", actualDateString())
                .addJobParameter("path", "", String.class, false)
                .addJobParameter("id", arquivo.getId(), Long.class, true)
                .toJobParameters();
        jobLauncher.run(jobCnab, parameters);
    }

}
