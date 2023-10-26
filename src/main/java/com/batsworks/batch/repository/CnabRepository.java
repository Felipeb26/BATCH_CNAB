package com.batsworks.batch.repository;

import com.batsworks.batch.domain.entity.CnabEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface CnabRepository extends JpaRepository<CnabEntity, Long> {

    @Query("SELECT COUNT(ce) FROM cnab ce WHERE ce.idArquivo.id=?1")
    Long countCnabsByIdArquivo(Long idArquivo);
    @Query("select sum(c.valorTitulo) from cnab c where c.idArquivo.id=?1")
    BigDecimal findValorTotalByIdArquivo(Long idArquivo);
}
