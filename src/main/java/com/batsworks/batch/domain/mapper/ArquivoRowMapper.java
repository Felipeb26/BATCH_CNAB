package com.batsworks.batch.domain.mapper;

import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class ArquivoRowMapper implements RowMapper<Arquivo> {
    public Arquivo mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Arquivo(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("extension"),
                resultSet.getString("fileSize"),
                resultSet.getLong("quantidade"),
                discoverStatus(resultSet.getString("situacao")),
                resultSet.getString("arquivo"),
                Collections.emptySet(),
                Collections.emptySet()
        );
    }

    private Status discoverStatus(String situacao) {
        if (situacao == null || situacao.isBlank()) return null;
        for (var status : Status.values()) {
            if (status.equals(situacao)) return status;
        }
        return Status.UNKNOWN;
    }
}
