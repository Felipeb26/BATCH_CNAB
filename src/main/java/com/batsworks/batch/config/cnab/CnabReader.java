package com.batsworks.batch.config.cnab;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import static java.util.Objects.isNull;

@Slf4j
public class CnabReader<T> extends FlatFileItemReader<T> {

    private byte[] myResource;

    public void setCustomResource(byte[] myResource) {
        this.myResource = myResource;
    }


    @Override
    public void setResource(Resource resource) {
        resource = isNull(myResource) ? resource : new ByteArrayResource(myResource);
        super.setResource(resource);
    }

    @Override
    public void setStrict(boolean strict) {
        super.setStrict(false);
    }

}
