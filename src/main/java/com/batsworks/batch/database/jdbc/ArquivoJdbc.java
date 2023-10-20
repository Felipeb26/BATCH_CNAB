package com.batsworks.batch.database.jdbc;

import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.mapper.ArquivoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ArquivoJdbc implements JdbcArquivo<Arquivo> {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Arquivo> findById(Long id) {
        try {
            String sql = """
                        select * from arquivo  where id in (?) for update;
                    """;
            return jdbcTemplate.query(sql, new ArquivoRowMapper(), id).stream().findFirst();
        } catch (Exception e) {
            log.error("ERROR: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean updateSituacao(Arquivo arquivo) {
        try {
            String sql = """
                        update arquivo set situacao=? where id in (?);
                    """;
            jdbcTemplate.update(sql, arquivo.getSituacao().name(), arquivo.getId());
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

}
