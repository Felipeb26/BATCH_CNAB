package com.batsworks.batch.config.amqp;

import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.Status;
import com.batsworks.batch.service.CnabService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class CnabFilaRead {

    private final CnabService cnabService;
    private final JobLauncher jobLauncher;
    private final Job jobCnab;

    @RabbitListener(queues = "arquivo.cnab")
    public void receveFile(Arquivo arquivoFila) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        var arquivo = cnabService.findArquivoByID(arquivoFila.getId());
        if (arquivo.getSituacao().equals(Status.PROCESSADO_SUCESSO)) {
            log.info("Arquivo {} já processado com sucesso nova tentativa realizada as {}", arquivo.getNome(), actualDateString());
            log.info("Apenas arquivo que não foram processado com sucesso podem ser reprocessados novamente");
            return;
        }

        JobParameters parameters = new JobParametersBuilder()
                .addString("time", actualDateString())
                .addJobParameter("path", arquivo.getNome(), String.class, false)
                .addJobParameter("id", arquivo.getId(), Long.class, true)
                .toJobParameters();
        jobLauncher.run(jobCnab, parameters);
    }

}
