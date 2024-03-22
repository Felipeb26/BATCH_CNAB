package com.batsworks.batch.service;

import com.batsworks.batch.domain.entity.CnabErro;
import com.batsworks.batch.domain.records.PageDTO;
import org.springframework.data.domain.Pageable;


public interface CnabErrorService {

    PageDTO errosPerPage(Pageable pageable);

    void saveCnabErro(CnabErro cnabErro);
}
