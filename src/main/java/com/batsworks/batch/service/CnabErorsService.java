package com.batsworks.batch.service;

import com.batsworks.batch.domain.records.PageDTO;
import org.springframework.data.domain.Pageable;


public interface CnabErorsService {

    PageDTO errosPerPage(Pageable pageable);
}
