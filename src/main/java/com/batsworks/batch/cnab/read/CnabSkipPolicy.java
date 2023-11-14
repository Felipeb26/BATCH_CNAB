package com.batsworks.batch.cnab.read;


import com.batsworks.batch.config.exception.CnabException;
import com.batsworks.batch.domain.entity.BatchParameters;
import com.batsworks.batch.domain.entity.CnabErro;
import com.batsworks.batch.repository.ArquivoRepository;
import com.batsworks.batch.repository.CnabErroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

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
        Long id = (long) parameters.get("id");

        if (t instanceof FlatFileParseException parseException) {
            var line = startWith(parseException.getInput());
            if (line) return true;
        }

        if (t.getCause() instanceof IOException) {
            log.error("## AN ILLEGAL ERROR HAS HAPPEN: {}, id: {}\n", t.getMessage(), id);
            return true;
        }

        var arquivo = arquivoRepository.findById(id).orElse(null);
        if (t.getCause() instanceof CnabException cnab) {
            cnabErroRepository.save(CnabErro.builder()
                    .arquivo(arquivo)
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

    private boolean startWith(String compare) {
        return compare.startsWith("01");
    }


}
