package com.batsworks.batch.service.impl;

import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.domain.entity.CnabErro;
import com.batsworks.batch.domain.mapper.CnabErrorMapper;
import com.batsworks.batch.domain.records.PageDTO;
import com.batsworks.batch.repository.CnabErroRepository;
import com.batsworks.batch.service.CnabErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class CnabErrorServiceImpl implements CnabErrorService {

    private final CnabErroRepository cnabErroRepository;

    public PageDTO errosPerPage(Pageable pageable) {
        Page<CnabErro> page = cnabErroRepository.findAll(pageable);
        if (page.isEmpty())
            throw new BussinesException(HttpStatus.BAD_REQUEST, "Não foi encontrado");
        return new PageDTO<>(page, CnabErrorMapper::cnabErrosToDTO);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public CnabErro savCnabErro(CnabErro cnabErro) {
        var erro = cnabErroRepository.findByNumeroLinhaAndIdArquivo(cnabErro.getLineNumber(), cnabErro.getArquivo().getId());
        if (nonNull(erro)) return null;
        return cnabErroRepository.save(cnabErro);
    }

}
