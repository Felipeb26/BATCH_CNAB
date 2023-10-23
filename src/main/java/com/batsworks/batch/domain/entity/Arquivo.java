package com.batsworks.batch.domain.entity;

import com.batsworks.batch.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "arquivo")
@AllArgsConstructor
@NoArgsConstructor
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Arquivo implements Serializable, Persistable<Long> {

    @Id
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
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idArquivo")
    private Set<CnabEntity> cnab;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idArquivo")
    private Set<CnabErro> cnabErros;

    @Override
    public boolean isNew() {
        return false;
    }
}

