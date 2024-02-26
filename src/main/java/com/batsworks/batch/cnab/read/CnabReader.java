package com.batsworks.batch.cnab.read;

import lombok.Setter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import static java.util.Objects.nonNull;

@Setter
public class CnabReader<T> extends FlatFileItemReader<T> {

    private byte[] stream;

    @Override
    public void setStrict(boolean strict) {
        super.setStrict(false);
    }

    @Override
    public void setResource(Resource resource) {
        if (nonNull(stream)) {
            super.setResource(new ByteArrayResource(stream));
        } else {
            super.setResource(resource);
        }
    }

}
