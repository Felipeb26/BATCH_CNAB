package com.batsworks.batch.repository;

import com.batsworks.batch.domain.entity.CnabErro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CnabErroRepository extends JpaRepository<CnabErro, Long> {
    @Query("SELECT COUNT(c) FROM CnabErro c WHERE c.idArquivo.id=?1")
    Long countCnabsByIdArquivo(Long idArquivo);
}
