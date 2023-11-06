package com.batsworks.batch.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@Builder
@Entity(name = "Cnab")
@Table(name = "cnab")
@AllArgsConstructor
@NoArgsConstructor
public class CnabEntity extends AbstractEntity<Arquivo> {

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
    private Long nossoNumero;
    private String digitoConferenciaNumeroBanco;
    private Long descontoDia;
    private Long condicaoEmpissaoPapeladaCobranca;
    private String boletoDebitoAutomatico;
    private Long identificacaoOcorrencia;
    private String numeroDocumento;
    private Date dataVencimento;
    private BigDecimal valorTitulo;
    private Long especieTitulo;
    private Date dataEmissao;
    private String primeiraInstrucao;
    private String segundaInstrucao;
    private BigDecimal moraDia;
    private Date dataLimiteDescontoConcessao;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArquivo")
    private Arquivo arquivo;
}
