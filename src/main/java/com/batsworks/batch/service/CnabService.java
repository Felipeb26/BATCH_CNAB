package com.batsworks.batch.service;

import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.entity.CnabEntity;
import com.batsworks.batch.domain.enums.CnabType;
import com.batsworks.batch.domain.records.DefaultMessage;
import org.springframework.web.multipart.MultipartFile;

public interface CnabService {
    DefaultMessage uploadCnabFile(MultipartFile file, CnabType tipo);

    byte[] downloadCnab(Boolean retorno, Long idArquivo);

    Arquivo findArquivoByID(Long id);

    String resetTempFile();

}
