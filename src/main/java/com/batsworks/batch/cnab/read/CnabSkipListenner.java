package com.batsworks.batch.cnab.read;

import com.batsworks.batch.domain.records.Cnab;
import com.batsworks.batch.domain.records.Cnab400;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;

@Slf4j
public class CnabSkipListenner implements SkipListener<Cnab400, Cnab> {

    @Override
    public void onSkipInRead(Throwable t) {
        log.warn("READ ERRO: {}", t.getMessage());
        SkipListener.super.onSkipInRead(t);
    }

    @Override
    public void onSkipInProcess(Cnab400 item, Throwable t) {
        log.warn("PROCESS ERRO: {}", t.getMessage());
        SkipListener.super.onSkipInProcess(item, t);
    }

    @Override
    public void onSkipInWrite(Cnab item, Throwable t) {
        log.warn("WRITE ERRO: {}", t.getMessage());
        SkipListener.super.onSkipInWrite(item, t);
    }

}
