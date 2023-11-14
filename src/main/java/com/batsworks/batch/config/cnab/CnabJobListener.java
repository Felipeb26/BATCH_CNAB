package com.batsworks.batch.config.cnab;

import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.domain.enums.Status;
import com.batsworks.batch.repository.ArquivoRepository;
import com.batsworks.batch.repository.CnabErroRepository;
import com.batsworks.batch.repository.CnabRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import static com.batsworks.batch.utils.Utilities.*;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
public class CnabJobListener implements JobExecutionListener {

    @Autowired
    private CnabErroRepository cnabErroRepository;

    @Autowired
    private CnabRepository cnabRepository;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("==========================> STARTED TO UPDATE FILE AT {}", actualDateString());
        var map = jobExecution.getJobParameters();

        if (map.isEmpty()) throw new BussinesException(BAD_REQUEST, "JobParameters is Null");
        var path = map.getString("path");

        if (path == null) throw new BussinesException(BAD_REQUEST, "File Path is Null");
        Long id = map.getLong("id", Long.valueOf(resolveFileName(path, true)));

        var arquivo = arquivoRepository.findById(requireNonNull(id))
                .orElseThrow(() -> new BussinesException(BAD_REQUEST, "ARQUIVO NÂO ENCONTRADO"));

        var erros = cnabErroRepository.countCnabsByIdArquivo(id);
        var boletos = cnabRepository.countCnabsByIdArquivo(id);
        var valorTotal = cnabRepository.findValorTotalByIdArquivo(id);

        arquivo.setSituacao(nonNull(erros) && erros > 0 ? Status.PROCESSADO_ERRO : Status.PROCESSADO_SUCESSO);
        arquivo.setQuantidade(erros + boletos);
        arquivo.setValorTotal(valorTotal);
        arquivoRepository.save(arquivo);

        deleteFile(path);
        log.info("==========================> FINISHED TO UPDATE FILE AT {}", actualDateString());
        JobExecutionListener.super.afterJob(jobExecution);
    }
}
