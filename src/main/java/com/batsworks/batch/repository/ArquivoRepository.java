package com.batsworks.batch.repository;

import com.batsworks.batch.domain.entity.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

    @Query("SELECT a.file FROM Arquivo a WHERE id=?1 ")
    Optional<byte[]> findArquivoById(Long id);
}
