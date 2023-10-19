package com.batsworks.batch.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
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

}
