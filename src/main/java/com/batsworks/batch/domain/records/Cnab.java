package com.batsworks.batch.domain.records;


import com.batsworks.batch.domain.entity.Arquivo;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;

import static com.batsworks.batch.config.utils.Utilities.parseDate;

public record Cnab(
        Integer id,
        String identRegistro,
        String agenciaDebito,
        String digitoAgencia,
        String razaoAgencia,
        String contaCorrente,
        String digitoConta,
        String identBeneficiario,
        String controleParticipante,
        String codigoBanco,
        Integer campoMulta,
        BigDecimal percentualMulta,
        Long nossoNumero,
        String digitoConferenciaNumeroBanco,
        Long descontoDia,
        Long condicaoEmpissaoPapeladaCobranca,
        String boletoDebitoAutomatico,
        Long identificacaoOcorrencia,
        String numeroDocumento,
        Date dataVencimento,
        BigDecimal valorTitulo,
        Long especieTitulo,
        Date dataEmissao,
        String primeiraInstrucao,
        String segundaInstrucao,
        BigDecimal moraDia,
        Date dataLimiteDescontoConcessao,
        BigDecimal valorDesconto,
        BigDecimal valorIOF,
        String valorAbatimento,
        Long tipoPagador,
        String nomePagador,
        String endereco,
        String primeiraMensagem,
        String cep,
        String sufixoCEP,
        String segundaMensagem,
        String sequencialRegistro,
        Arquivo arquivo) {

    public Cnab withDates(String dataVencimento, String dataEmissao, String dataLimiteDescontoConcessao) throws ParseException {
        var dataLimiteDescontoConcessa = parseDate(dataLimiteDescontoConcessao);
        var dataVenciment = parseDate(dataVencimento);
        var dataEmissa = parseDate(dataEmissao);

        return new Cnab(id, identRegistro, agenciaDebito, digitoAgencia, razaoAgencia, contaCorrente, digitoConta,
                identBeneficiario, controleParticipante, codigoBanco, campoMulta, percentualMulta, nossoNumero,
                digitoConferenciaNumeroBanco, descontoDia, condicaoEmpissaoPapeladaCobranca, boletoDebitoAutomatico,
                identificacaoOcorrencia, numeroDocumento, dataVenciment, valorTitulo, especieTitulo, dataEmissa,
                primeiraInstrucao, segundaInstrucao, moraDia, dataLimiteDescontoConcessa, valorDesconto, valorIOF,
                valorAbatimento, tipoPagador, nomePagador, endereco, primeiraMensagem, cep, sufixoCEP, segundaMensagem, sequencialRegistro, arquivo);
    }

    public Cnab withArquivo(Arquivo arquivoNovo){
        return new Cnab(id, identRegistro, agenciaDebito, digitoAgencia, razaoAgencia, contaCorrente, digitoConta,
                identBeneficiario, controleParticipante, codigoBanco, campoMulta, percentualMulta, nossoNumero,
                digitoConferenciaNumeroBanco, descontoDia, condicaoEmpissaoPapeladaCobranca, boletoDebitoAutomatico,
                identificacaoOcorrencia, numeroDocumento, dataVencimento, valorTitulo, especieTitulo, dataEmissao,
                primeiraInstrucao, segundaInstrucao, moraDia, dataLimiteDescontoConcessao, valorDesconto, valorIOF,
                valorAbatimento, tipoPagador, nomePagador, endereco, primeiraMensagem, cep, sufixoCEP, segundaMensagem, sequencialRegistro, arquivoNovo);
    }

}