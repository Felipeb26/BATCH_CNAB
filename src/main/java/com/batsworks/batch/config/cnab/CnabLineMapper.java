package com.batsworks.batch.config.cnab;

import com.batsworks.batch.config.exception.CnabException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;

public class CnabLineMapper<T> extends DefaultLineMapper<T> {

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        try {
            if (line.length() > 400)
                throw new CnabException("Only allowed 400 caracteres to cnab 400", lineNumber, line);
            return super.mapLine(line, lineNumber);
        } catch (Exception e) {
            throw new CnabException(e.getMessage(), lineNumber, line);
        }
    }

}
