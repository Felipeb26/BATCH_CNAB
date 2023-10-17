package com.batsworks.batch.config.cnab;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import static java.util.Objects.nonNull;

public class CnabReader<T> extends FlatFileItemReader<T> {
    private static byte[] stream;

    @Override
    public void setResource(Resource resource) {
        if (nonNull(stream)) {
            super.setResource(new ByteArrayResource(stream));
        } else super.setResource(resource);
    }

    public void setStream(byte[] stream) {
        if (nonNull(stream)) this.stream = stream;
    }

    @Override
    public void setStrict(boolean strict) {
        super.setStrict(false);
    }
}
