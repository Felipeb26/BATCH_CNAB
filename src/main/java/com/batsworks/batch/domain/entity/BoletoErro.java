package com.batsworks.batch.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@Entity
@Table(name = "BOLETO_ERRO")
@AllArgsConstructor
@NoArgsConstructor
public class BoletoErro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private Long line;
}
