package com.batsworks.batch.domain.mapper;

import com.batsworks.batch.domain.dto.CnabErroDTO;
import com.batsworks.batch.domain.entity.CnabErro;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CnabMapper {

    public static List<CnabErroDTO> cnabErrosToDTO(List<CnabErro> cnabErros) {
        return cnabErros.stream().distinct().map(CnabMapper::cnabErroToDTO).toList();
    }

    public static CnabErroDTO cnabErroToDTO(CnabErro cnabErro) {
        return new CnabErroDTO(cnabErro.getLineNumber(), cnabErro.getLine(), cnabErro.getErro(), cnabErro.getMessage(), cnabErro.getDataCadastro());
    }

}
