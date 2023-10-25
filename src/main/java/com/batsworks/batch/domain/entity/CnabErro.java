package com.batsworks.batch.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@Builder
@Entity
@Table(name = "cnab_erro")
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class CnabErro  extends AbstractEntity<Arquivo> {

    private String message;
    private String erro;
    private Long lineNumber;
    private String line;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArquivo")
    private Arquivo idArquivo;

}
