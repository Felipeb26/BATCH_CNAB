package com.batsworks.batch.repository;

import com.batsworks.batch.domain.entity.CnabEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CnabRepository extends CrudRepository<CnabEntity, Long>, PagingAndSortingRepository<CnabEntity, Long> {

    @Query("FROM CnabEntity ce WHERE ce.idArquivo.id=?1")
    Optional<List<CnabEntity>> findAllByIdArquivo(Long idArquivo);

    @Query("SELECT COUNT(*) FROM CnabEntity ce WHERE ce.idArquivo.id=?1")
    Long countCnabsByIdArquivo(Long idArquivo);
}
