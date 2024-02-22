package com.batsworks.batch.service;

import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.CnabType;
import com.batsworks.batch.domain.records.ArquivoDTO;
import com.batsworks.batch.domain.records.DefaultMessage;
import com.batsworks.batch.domain.records.PageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ArquivoService {

    DefaultMessage uploadCnabFile(MultipartFile file, CnabType tipo, String observcao);

    byte[] downloadCnab(Boolean retorno, Long idArquivo);

    ArquivoDTO findArquivoByID(Long id);

    Arquivo findArquivoEntityById(Long id);

    void deleteArquivo(Long id);

    PageDTO findAllByPage(Pageable pageable);
}
