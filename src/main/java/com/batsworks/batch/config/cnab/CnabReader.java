package com.batsworks.batch.config.cnab;

import org.springframework.batch.item.file.FlatFileItemReader;

public class CnabReader<T> extends FlatFileItemReader<T> {

    @Override
    public void setStrict(boolean strict) {
        super.setStrict(false);
    }

}
