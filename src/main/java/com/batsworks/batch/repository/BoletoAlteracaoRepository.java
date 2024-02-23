package com.batsworks.batch.repository;

import com.batsworks.batch.domain.entity.BoletoAlteracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoletoAlteracaoRepository extends JpaRepository<BoletoAlteracao, Long> {

    @Modifying
    @Query("DELETE FROM BoletoAlteracao c WHERE c.arquivo.id=?1")
    void deleteAllByIdArquivo(Long id);
}
