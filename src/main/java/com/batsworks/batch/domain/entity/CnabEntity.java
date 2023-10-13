package com.batsworks.batch.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Entity
@Table(name = "CNAB")
@AllArgsConstructor
@NoArgsConstructor
public class CnabEntity {
    @Id
    Long id;
    @Column(name = "IDENTREGISTRO")
    String identRegistro;
    @Column(name = "AGENCIADEBITO")
    String agenciaDebito;
    @Column(name = "DIGITOAGENCIA")
    String digitoAgencia;
    @Column(name = "RAZAOAGENCIA")
    String razaoAgencia;
    @Column(name = "CONTACORRENTE")
    String contaCorrente;
    @Column(name = "DIGITOCONTA")
    String digitoConta;
    @Column(name = "IDENTBENEFICIARIO")
    String identBeneficiario;
    @Column(name = "CONTROLEPARTICIPANTE")
    String controleParticipante;
    @Column(name = "CODIGOBANCO")
    String codigoBanco;
    @Column(name = "CAMPOMULTA")
    Integer campoMulta;
    @Column(name = "PERCENTUALMULTA")
    BigDecimal percentualMulta;
    @Column(name = "NOSSONUMERO")
    Long nossoNumero;
    @Column(name = "DIGITOCONFERENCIANUMEROBANCO")
    String digitoConferenciaNumeroBanco;
    @Column(name = "DESCONTODIA")
    Long descontoDia;
    @Column(name = "CONDICAOEMPISSAOPAPELADACOBRANCA")
    Long condicaoEmpissaoPapeladaCobranca;
    @Column(name = "BOLETODEBITOAUTOMATICO")
    String boletoDebitoAutomatico;
    @Column(name = "IDENTIFICACAOOCORRENCIA")
    Long identificacaoOcorrencia;
    @Column(name = "NUMERODOCUMENTO")
    String numeroDocumento;
    @Column(name = "DATAVENCIMENTO")
    Date dataVencimento;
    @Column(name = "VALORTITULO")
    BigDecimal valorTitulo;
    @Column(name = "ESPECIETITULO")
    Long especieTitulo;
    @Column(name = "DATAEMISSAO")
    Date dataEmissao;
    @Column(name = "PRIMEIRAINSTRUCAO")
    String primeiraInstrucao;
    @Column(name = "SEGUNDAINSTRUCAO")
    String segundaInstrucao;
    @Column(name = "MORADIA")
    BigDecimal moraDia;
    @Column(name = "DATALIMITEDESCONTOCONCESSAO")
    Date dataLimiteDescontoConcessao;
    @Column(name = "VALORDESCONTO")
    BigDecimal valorDesconto;
    @Column(name = "VALORIOF")
    BigDecimal valorIOF;
    @Column(name = "VALORABATIMENTO")
    String valorAbatimento;
    @Column(name = "TIPOPAGADOR")
    Long tipoPagador;
    @Column(name = "NOMEPAGADOR")
    String nomePagador;
    @Column(name = "ENDERECO")
    String endereco;
    @Column(name = "PRIMEIRAMENSAGEM")
    String primeiraMensagem;
    @Column(name = "CEP")
    String cep;
    @Column(name = "SUFIXOCEP")
    String sufixoCEP;
    @Column(name = "SEGUNDAMENSAGEM")
    String segundaMensagem;
    @Column(name = "SEQUENCIALREGISTRO")
    String sequencialRegistro;
}
