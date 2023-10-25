package com.batsworks.batch.domain.entity;

import com.batsworks.batch.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "arquivo")
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Arquivo extends AbstractEntity<Arquivo> {

    private String name;
    private String extension;
    @Column(name = "fileSize")
    private String fileSize;
    private Long quantidade;
    @Enumerated(EnumType.STRING)
    private Status situacao;
    @Column(name = "arquivo")
    private String file;
    private BigDecimal valorTotal;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idArquivo")
    private Set<CnabEntity> cnab;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idArquivo")
    private Set<CnabErro> cnabErros;

}

