package com.batsworks.batch.repository;

import com.batsworks.batch.domain.entity.CnabErro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CnabErroRepository extends CrudRepository<CnabErro, Long> {
    @Query("SELECT COUNT(c) FROM CnabErro c WHERE c.idArquivo.id=?1")
    Long countCnabsByIdArquivo(Long idArquivo);
}
