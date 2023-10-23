package com.batsworks.batch.database.repository;

import com.batsworks.batch.domain.entity.CnabEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CnabRepository extends JpaRepository<CnabEntity, Long> {

    @Query("FROM CnabEntity ce WHERE ce.idArquivo.id=?1")
    Optional<List<CnabEntity>> findAllByIdArquivo(Long idArquivo);
    @Query("SELECT COUNT(ce) FROM CnabEntity ce WHERE ce.idArquivo.id=?1")
    Long countCnabsByIdArquivo(Long idArquivo);
}
