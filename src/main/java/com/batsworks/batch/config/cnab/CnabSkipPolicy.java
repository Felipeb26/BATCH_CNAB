package com.batsworks.batch.config.cnab;

import com.batsworks.batch.config.exception.CnabException;
import com.batsworks.batch.domain.entity.CnabErro;
import com.batsworks.batch.repository.CnabErroRepository;
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

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        log.error("AN ERROR HAS HAPPEN: {}", t.getMessage());
        if (t instanceof IllegalArgumentException) {
            log.error("AN ILLEGAL ERROR HAS HAPPEN: {}", t.getMessage());
        }
        if (t.getCause() instanceof CnabException cnab) {
            var erro = CnabErro.builder()
                    .erro(cnab.getMessage())
                    .message(cnab.getMessage().concat(" - was received: ").concat(String.valueOf(cnab.getSize())))
                    .lineNumber(cnab.getActualLine())
                    .line(cnab.getLine())
                    .build();
            cnabErroRepository.save(erro);
            return true;
        }else {
            log.error("AN UNCOMMON ERROR HAS HAPPEN: {}", t.getMessage());
        }
        return false;
    }
}
