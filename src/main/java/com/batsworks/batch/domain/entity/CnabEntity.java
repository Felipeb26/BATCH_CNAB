package com.batsworks.batch.domain.entity;

import com.batsworks.batch.domain.enums.SituacaoCnab;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity(name = "Cnab")
@Table(name = "cnab")
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class CnabEntity extends AbstractEntity<CnabEntity> implements Serializable {

    private String identRegistro;
    private String agenciaDebito;
    private String digitoAgencia;
    private String razaoAgencia;
    private String contaCorrente;
    private String digitoConta;
    private String identBeneficiario;
    private String controleParticipante;
    private String codigoBanco;
    private Integer campoMulta;
    private BigDecimal percentualMulta;
    private String nossoNumero;
    private String digitoConferenciaNumeroBanco;
    private Long descontoDia;
    private Long condicaoEmpissaoPapeladaCobranca;
    private String boletoDebitoAutomatico;
    private Long identificacaoOcorrencia;
    private String numeroDocumento;
    private LocalDate dataVencimento;
    private BigDecimal valorTitulo;
    private Long especieTitulo;
    private LocalDate dataEmissao;
    private String primeiraInstrucao;
    private String segundaInstrucao;
    private BigDecimal moraDia;
    private LocalDate dataLimiteDescontoConcessao;
    private BigDecimal valorDesconto;
    private BigDecimal valorIOF;
    private String valorAbatimento;
    private Long tipoPagador;
    private String nomePagador;
    private String endereco;
    private String primeiraMensagem;
    private String cep;
    private String sufixoCEP;
    private String segundaMensagem;
    private String sequencialRegistro;
    private Long linha;
    @Enumerated(EnumType.STRING)
    private SituacaoCnab situacao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArquivo")
    private Arquivo arquivo;
}
