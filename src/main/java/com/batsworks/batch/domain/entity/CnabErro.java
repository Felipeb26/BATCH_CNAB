package com.batsworks.batch.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Entity
@Table(name = "cnab_erro")
@AllArgsConstructor
@NoArgsConstructor
public class CnabErro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private String erro;
    private Long lineNumber;
    private String line;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArquivo")
    private Arquivo idArquivo;

}
