package com.batsworks.batch.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.cache.annotation.Cacheable;


@Getter
@Setter
@Builder
@Entity
@Table(name = "cnab_erro")
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Cacheable(value = {"EntityCache"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CnabErro extends AbstractEntity<Arquivo> {

    private String message;
    private String erro;
    private Long lineNumber;
    private String line;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArquivo", nullable = false)
    private Arquivo arquivo;

}
