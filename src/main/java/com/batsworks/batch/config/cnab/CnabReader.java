package com.batsworks.batch.config.cnab;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.util.Objects.nonNull;

public class CnabReader<T> extends FlatFileItemReader<T>  {
    private byte[] stream;

    @Override
    public void setResource(Resource resource) {
        if (nonNull(stream)) {
            InputStream is = new ByteArrayInputStream(stream);
            InputStreamResource res = new InputStreamResource(is);
            super.setResource(res);
        } else super.setResource(resource);
    }


    public void setStream(byte[] stream) {
        this.stream = stream;
    }

}
