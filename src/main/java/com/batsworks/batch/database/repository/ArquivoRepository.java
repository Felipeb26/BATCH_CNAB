package com.batsworks.batch.database.repository;

import com.batsworks.batch.domain.entity.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {
}
