package com.batsworks.batch.repository;

import com.batsworks.batch.domain.entity.CnabEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CnabRepository extends JpaRepository<CnabEntity, Long>, PagingAndSortingRepository<CnabEntity, Long> {

    @Query("FROM Cnab ce WHERE ce.arquivo.id=?1")
    Optional<List<CnabEntity>> findAllByIdArquivo(Long idArquivo);

    @Query("SELECT COUNT(ce.id) FROM Cnab ce WHERE ce.arquivo.id=?1")
    Long countCnabsByIdArquivo(Long idArquivo);

    @Query("SELECT SUM(c.valorTitulo) FROM Cnab c WHERE c.arquivo.id=?1")
    BigDecimal findValorTotalByIdArquivo(Long idArquivo);

    @Query(value = "SELECT c FROM FROM Cnab c WHERE c.arquivo.id=?1",
            countQuery = "SELECT count(c.id) FROM FROM Cnab c WHERE c.arquivo.id=?1")
    Page<CnabEntity> findAllById(Long id, Pageable pageable);
}
