package com.batsworks.batch.config.cnab;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;

@Slf4j
public class CnabReader<T> extends FlatFileItemReader<T> {

    @Override
    public void setStrict(boolean strict) {
        super.setStrict(false);
    }

}
