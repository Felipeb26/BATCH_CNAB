package com.batsworks.batch.service;

import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.domain.entity.CnabErro;
import com.batsworks.batch.domain.mapper.CnabMapper;
import com.batsworks.batch.domain.records.PageDTO;
import com.batsworks.batch.repository.CnabErroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CnabErorsService {


    private final CnabErroRepository cnabErroRepository;

    public Object string(Pageable pageable) {
        Page<CnabErro> page = cnabErroRepository.findAll(pageable);
        if (page.isEmpty())
            throw new BussinesException(HttpStatus.BAD_REQUEST, "Não foi encontrado");
        return new PageDTO<>(page, CnabMapper::cnabErrosToDTO);
    }
}
