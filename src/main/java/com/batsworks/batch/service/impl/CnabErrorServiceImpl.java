package com.batsworks.batch.service.impl;

import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.domain.entity.CnabErro;
import com.batsworks.batch.domain.mapper.CnabMapper;
import com.batsworks.batch.domain.records.PageDTO;
import com.batsworks.batch.repository.CnabErroRepository;
import com.batsworks.batch.service.CnabErorsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CnabErrorServiceImpl implements CnabErorsService {

    private final CnabErroRepository cnabErroRepository;

    public PageDTO errosPerPage(Pageable pageable) {
        Page<CnabErro> page = cnabErroRepository.findAll(pageable);
        if (page.isEmpty())
            throw new BussinesException(HttpStatus.BAD_REQUEST, "Não foi encontrado");
        return new PageDTO<>(page, CnabMapper::cnabErrosToDTO);
    }
}
