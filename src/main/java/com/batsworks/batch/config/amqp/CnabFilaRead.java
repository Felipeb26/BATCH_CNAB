package com.batsworks.batch.config.amqp;

import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.CnabStatus;
import com.batsworks.batch.service.ArquivoService;
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

    private final ArquivoService arquivoService;
    private final JobLauncher jobLauncher;
    private final Job jobCnab;

    @RabbitListener(queues = "arquivo.cnab")
    public void receveFile(Arquivo arquivoFila) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        var arquivo = arquivoService.findArquivoByID(arquivoFila.getId());
        if (arquivo.situacao().equals(CnabStatus.PROCESSADO_SUCESSO)) {
            log.info("Arquivo {} já processado com sucesso nova tentativa realizada as {}", arquivo.nome(), actualDateString());
            log.info("Apenas arquivo que não foram processado com sucesso podem ser reprocessados novamente");
            return;
        }

        JobParameters parameters = new JobParametersBuilder()
                .addString("time", actualDateString())
                .addJobParameter("path", arquivo.nome(), String.class, false)
                .addJobParameter("id", arquivo.id(), Long.class, false)
                .toJobParameters();
        jobLauncher.run(jobCnab, parameters);
    }

}
