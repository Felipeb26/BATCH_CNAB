package com.batsworks.batch.domain.entity;

import com.batsworks.batch.domain.enums.CnabStatus;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name = "arquivo")
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Cacheable(value = {"EntityCache"})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Arquivo extends AbstractEntity<Arquivo> implements Serializable {

    private String nome;
    private String extension;
    @Column(name = "fileSize")
    private Long fileSize;
    private Long quantidade;
    @Enumerated(EnumType.STRING)
    private CnabStatus situacao;
    @Lob
    @Nonnull
    @Column(name = "arquivo")
    private byte[] file;
    private BigDecimal valorTotal;
    private String observacao;
}

