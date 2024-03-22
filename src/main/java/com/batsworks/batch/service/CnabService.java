package com.batsworks.batch.service;

import com.batsworks.batch.config.exception.CnabProcessingException;
import com.batsworks.batch.domain.records.Cnab;

public interface CnabService {
    void ocorrencia02(Cnab cnab) throws CnabProcessingException, IllegalAccessException;

    void ocorrencia04(Cnab cnab) throws CnabProcessingException, IllegalAccessException;

    void ocorrencia05(Cnab cnab) throws CnabProcessingException, IllegalAccessException;

    void ocorrencia06(Cnab cnab) throws CnabProcessingException, IllegalAccessException;

    void ocorrencia07(Cnab cnab) throws CnabProcessingException, IllegalAccessException;

    void ocorrencia08(Cnab cnab) throws CnabProcessingException, IllegalAccessException;

    void ocorrencia20(Cnab cnab) throws CnabProcessingException, IllegalAccessException;
}
