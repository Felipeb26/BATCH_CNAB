package com.batsworks.batch.domain.entity;

import com.batsworks.batch.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "arquivo")
@AllArgsConstructor
@NoArgsConstructor
public class Arquivo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String extension;
    @Column(name = "fileSize")
    private String fileSize;
    private Long quantidade;
    @Enumerated(EnumType.STRING)
    private Status situacao;
    @Column(name = "arquivo")
    private String file;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "arquivo")
    private Set<CnabEntity> cnab;

}
