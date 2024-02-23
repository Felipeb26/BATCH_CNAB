package com.batsworks.batch.domain.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "boleto_alteracao")
@AllArgsConstructor
@NoArgsConstructor
public class BoletoAlteracao extends AbstractEntity<BoletoAlteracao> {

    private String camposALterados;
    private String camposAntigos;
    private String tipoDeAlteracao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArquivo")
    private Arquivo arquivo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boletoAlterado")
    private CnabEntity boletoAlterado;
}
