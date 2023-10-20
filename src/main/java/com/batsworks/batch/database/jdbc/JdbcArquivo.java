package com.batsworks.batch.database.jdbc;

import java.util.Optional;

public interface JdbcArquivo<T> {
    Optional<T> findById(Long id);

    boolean updateSituacao(T t);

}
