package com.batsworks.batch.domain.entity;


import com.batsworks.batch.domain.enums.Zones;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class AbstractEntity<T> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private Long id;
    @CreatedDate
    @Getter(AccessLevel.PUBLIC)
    @Setter(AccessLevel.PUBLIC)
    private LocalDateTime dataCadastro;

    @PrePersist
    private void persistDate() {
        dataCadastro = LocalDateTime.now(Zones.AMERIACA_SAO_PAULO.getZone());
    }
}