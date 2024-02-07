package com.batsworks.batch.domain.entity;

import com.batsworks.batch.domain.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

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

    private String name;
    private String extension;
    @Column(name = "fileSize")
    private Long fileSize;
    private Long quantidade;
    @Enumerated(EnumType.STRING)
    private Status situacao;
    @Lob
    @Nonnull
    @Column(name = "arquivo")
    private byte[] file;
    private BigDecimal valorTotal;
    private String observacao;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "arquivo")
    @JsonBackReference
    @ToString.Exclude
    private Set<CnabEntity> cnab;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "arquivo")
    @JsonBackReference
    @ToString.Exclude
    private Set<CnabErro> cnabErros;

}

