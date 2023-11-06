package com.batsworks.batch.repository;

import com.batsworks.batch.domain.entity.CnabEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CnabRepository extends CrudRepository<CnabEntity, Long>, PagingAndSortingRepository<CnabEntity, Long> {

    @Query("FROM Cnab ce WHERE ce.arquivo.id=?1")
    Optional<List<CnabEntity>> findAllByIdArquivo(Long idArquivo);

    @Query("SELECT COUNT(*) FROM Cnab ce WHERE ce.arquivo.id=?1")
    Long countCnabsByIdArquivo(Long idArquivo);

    @Query("SELECT SUM(c.valorTitulo) FROM Cnab c WHERE c.arquivo.id=?1")
    BigDecimal findValorTotalByIdArquivo(Long idArquivo);
}
