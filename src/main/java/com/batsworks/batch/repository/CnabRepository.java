package com.batsworks.batch.repository;

import com.batsworks.batch.domain.entity.CnabEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;

public interface CnabRepository extends JpaRepository<CnabEntity, Long>, PagingAndSortingRepository<CnabEntity, Long> {

    @Query("SELECT ce.linha FROM Cnab ce WHERE ce.arquivo.id=?1 order by linha desc limit 1")
    Long findLastByIdArquivo(Long idArquivo);

    @Query("SELECT COUNT(ce.id) FROM Cnab ce WHERE ce.arquivo.id=?1")
    Long countCnabsByIdArquivo(Long idArquivo);

    @Query("SELECT SUM(c.valorTitulo) FROM Cnab c WHERE c.arquivo.id=?1")
    BigDecimal findValorTotalByIdArquivo(Long idArquivo);

    @Query(value = "SELECT c FROM FROM Cnab c WHERE c.arquivo.id=?1",
            countQuery = "SELECT count(c.id) FROM FROM Cnab c WHERE c.arquivo.id=?1")
    Page<CnabEntity> findAllById(Long id, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Cnab c WHERE c.arquivo.id=?1")
    void deleteAllByIdArquivo(Long idArquivo);

}
