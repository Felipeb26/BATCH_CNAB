package com.batsworks.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.batsworks.batch.domain.entity.CnabErro;

public interface CnabErroRepository extends JpaRepository<CnabErro, Long> {
    @Query("SELECT COUNT(c) FROM CnabErro c WHERE c.arquivo.id=?1")
    Long countCnabsByIdArquivo(Long idArquivo);
}
