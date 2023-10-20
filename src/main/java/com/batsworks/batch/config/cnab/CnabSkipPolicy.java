package com.batsworks.batch.config.cnab;


import com.batsworks.batch.config.exception.CnabException;
import com.batsworks.batch.config.utils.BatchParameters;
import com.batsworks.batch.database.repository.ArquivoRepository;
import com.batsworks.batch.database.repository.CnabErroRepository;
import com.batsworks.batch.domain.entity.CnabErro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A linha que mostra no log assim como a salva nem sempre é a verdadeira já
 * que a aplicação usa multithread para paralelismo asyncrono
 */
@Slf4j
public class CnabSkipPolicy implements SkipPolicy {

    @Autowired
    private CnabErroRepository cnabErroRepository;
    @Autowired
    private ArquivoRepository arquivoRepository;
    @Autowired
    private BatchParameters batchParameters;

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        var parameters = batchParameters.getParameters();
        var arquivo = arquivoRepository.findById((Long) parameters.get("id")).orElse(null);
        if (t instanceof IllegalArgumentException) {
            log.error("AN ILLEGAL ERROR HAS HAPPEN: {}", t.getMessage());
        }
        if (t.getCause() instanceof CnabException cnab) {
            cnabErroRepository.save(CnabErro.builder()
                    .idArquivo(arquivo)
                    .erro(cnab.getMessage())
                    .message(cnab.getMessage().concat(" - was received: ").concat(String.valueOf(cnab.getSize())))
                    .lineNumber(cnab.getActualLine())
                    .line(cnab.getLine())
                    .build());
            return true;
        } else {
            log.error("AN UNCOMMON ERROR HAS HAPPEN: {}", t.getMessage());
        }
        return false;
    }

}
