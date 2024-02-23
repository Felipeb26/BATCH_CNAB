package com.batsworks.batch.service;

import com.batsworks.batch.config.exception.CnabProcessingException;
import com.batsworks.batch.domain.records.Cnab;

public interface CnabService {
    Boolean ocorrencia02(Cnab cnab) throws Exception;

    Boolean ocorrencia03(Cnab cnab) throws Exception;

    Boolean ocorrencia04(Cnab cnab) throws Exception;

    Boolean ocorrencia05(Cnab cnab) throws Exception;

    Boolean ocorrencia06(Cnab cnab) throws Exception;
}
