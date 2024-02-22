package com.batsworks.batch.domain.entity;


import com.batsworks.batch.domain.enums.Zones;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private Long id;
    @CreatedDate
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private LocalDateTime dataCadastro;
    @CreatedDate
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private LocalDateTime dataAtualizacao;

    @PrePersist
    private void persistDate() {
        this.dataCadastro = LocalDateTime.now(Zones.AMERIACA_SAO_PAULO.getZone());
    }

    @PreUpdate
    private void persistUpdate(){
        this.dataAtualizacao = LocalDateTime.now(Zones.AMERIACA_SAO_PAULO.getZone());
    }
}