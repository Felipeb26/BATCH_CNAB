package com.batsworks.batch.config.cnab;

import com.batsworks.batch.config.exception.CnabException;
import com.batsworks.batch.domain.entity.BoletoErro;
import com.batsworks.batch.repository.BoletoErroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CnabSkipPolicy implements SkipPolicy {

    @Autowired
    BoletoErroRepository boletoErroRepository;

    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        t = t.getCause();
        if (t instanceof IllegalArgumentException) {
            System.out.println(t.getMessage());
        }
        if (t instanceof CnabException cnab) {
            var erro = BoletoErro.builder()
                    .message(cnab.getMessage() + " received: " + cnab.getSize())
                    .line(cnab.getActualLine())
                    .build();
            boletoErroRepository.save(erro);
            return true;
        }
        return false;
    }
}
