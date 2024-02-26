package com.batsworks.batch.domain.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Entity
@Table(name = "boleto_alteracao")
@AllArgsConstructor
@NoArgsConstructor
public class BoletoAlteracao extends AbstractEntity<BoletoAlteracao> implements Serializable {

    private String camposAlterados;
    private String camposAntigos;
    private String tipoDeAlteracao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArquivo")
    @JsonManagedReference
    private Arquivo arquivo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boletoAlterado")
    private CnabEntity boletoAlterado;
}
