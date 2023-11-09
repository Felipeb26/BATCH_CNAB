package com.batsworks.batch.config.cnab;

import com.batsworks.batch.domain.enums.Status;
import com.batsworks.batch.repository.ArquivoRepository;
import com.batsworks.batch.repository.CnabErroRepository;
import com.batsworks.batch.repository.CnabRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import static com.batsworks.batch.config.utils.Utilities.deleteFile;
import static com.batsworks.batch.config.utils.Utilities.resolveFileName;
import static java.util.Objects.nonNull;

public class CnabTasklet implements Tasklet {
    @Autowired
    private CnabErroRepository cnabErroRepository;
    @Autowired
    private CnabRepository cnabRepository;
    @Autowired
    private ArquivoRepository arquivoRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        var map = chunkContext.getStepContext().getJobParameters();
        var path = (String) map.get("path");
        var id = Long.valueOf(resolveFileName(path, true));

        var arquivo = arquivoRepository.findById(id).orElse(null);
        if (arquivo == null) return RepeatStatus.CONTINUABLE;

        var erros = cnabErroRepository.countCnabsByIdArquivo(id);
        var boletos = cnabRepository.countCnabsByIdArquivo(id);
        var valorTotal = cnabRepository.findValorTotalByIdArquivo(id);

        arquivo.setSituacao(nonNull(erros) && erros > 0 ? Status.PROCESSADO_ERRO : Status.PROCESSADO_SUCESSO);
        arquivo.setQuantidade(erros + boletos);
        arquivo.setValorTotal(valorTotal);
        arquivoRepository.save(arquivo);

        if (deleteFile(path)) {
            return RepeatStatus.FINISHED;
        }
        return RepeatStatus.CONTINUABLE;
    }
}
