package com.batsworks.batch.config.cnab;

import com.batsworks.batch.database.repository.ArquivoRepository;
import com.batsworks.batch.database.repository.CnabErroRepository;
import com.batsworks.batch.database.repository.CnabRepository;
import com.batsworks.batch.domain.enums.Status;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Objects.isNull;

public class CnabTasklet implements Tasklet {
    @Autowired
    private CnabErroRepository cnabErroRepository;
    @Autowired
    private CnabRepository cnabRepository;
    @Autowired
    private ArquivoRepository arquivoRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        var id = (long) chunkContext.getStepContext().getJobParameters().get("id");

        var arquivo = arquivoRepository.findById(id).orElse(null);
        if (arquivo == null) return RepeatStatus.CONTINUABLE;

        var erros = cnabErroRepository.countCnabsByIdArquivo(id);
        var boletos = cnabRepository.countCnabsByIdArquivo(id);

        arquivo.setSituacao(isNull(erros) ? Status.PROCESSADO_ERRO : Status.PROCESSADO_SUCESSO);
        arquivo.setQuantidade(erros + boletos);
        arquivoRepository.save(arquivo);
        return RepeatStatus.FINISHED;
    }
}
