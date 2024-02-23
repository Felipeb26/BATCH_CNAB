package com.batsworks.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.batsworks.batch.domain.entity.CnabErro;

public interface CnabErroRepository extends JpaRepository<CnabErro, Long> {
    @Query("SELECT COUNT(c) FROM CnabErro c WHERE c.arquivo.id=?1")
    Long countCnabsByIdArquivo(Long idArquivo);

    @Modifying
    @Query("DELETE FROM CnabErro c WHERE c.arquivo.id=?1")
    void deleteAllByIdArquivo(Long idArquivo);

    @Query("FROM CnabErro ce WHERE ce.lineNumber=?1 and ce.arquivo.id=?2")
    CnabErro findByNumeroLinhaAndIdArquivo(Long lineNumber, Long idArquivo);
}
