package com.batsworks.batch.domain.mapper;

import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.records.ArquivoDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArquivoMapper {

    public List<ArquivoDTO> toDTO(List<Arquivo> arquivos){
        return arquivos.stream().map(this::toDTO).toList();
    }

    public List<Arquivo> toEntity(List<ArquivoDTO> arquivoDTOS){
        return arquivoDTOS.stream().map(this::toEntity).toList();
    }

    public ArquivoDTO toDTO(Arquivo arquivo) {
        return new ArquivoDTO(arquivo.getId(), arquivo.getNome(), arquivo.getExtension(),
                arquivo.getFileSize(), arquivo.getQuantidade(), arquivo.getSituacao(),
                arquivo.getObservacao(), arquivo.getValorTotal());
    }

    public Arquivo toEntity(ArquivoDTO arquivoDTO){
        return Arquivo.builder()
                .nome(arquivoDTO.nome())
                .extension(arquivoDTO.extension())
                .fileSize(arquivoDTO.fileSize())
                .quantidade(arquivoDTO.quantidade())
                .situacao(arquivoDTO.situacao())
                .observacao(arquivoDTO.observacao())
                .valorTotal(arquivoDTO.valorTotal())
                .build();
    }
}
