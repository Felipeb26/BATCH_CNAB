package com.batsworks.batch.cnab.write;

import com.batsworks.batch.domain.entity.CnabEntity;
import com.batsworks.batch.domain.records.Cnab400;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.Objects.isNull;

@Slf4j
public class CnabWriteProcessor implements ItemProcessor<CnabEntity, Cnab400> {

    @Override
    public Cnab400 process(CnabEntity item) {

//        return new Cnab400(
//                format(item.getIdentRegistro(), Campo.IDENTIFICACAO_DO_REGISTRO),
//                format(item.getAgenciaDebito(), Campo.AGENCIA_DE_DEBITO),
//                format(item.getDigitoAgencia(), Campo.DIGITO_DA_AGENCIA_DE_DEBITO),
//                format(item.getRazaoAgencia(), Campo.RAZAO_DA_CONTA_CORRENTE),
//                format(item.getContaCorrente(), Campo.CONTA_CORRENTE),
//                format(item.getDigitoConta(), Campo.DIGITO_DA_CONTA_CORRENTE),
//                format(item.getIdentBeneficiario(), Campo.IDENTIFICACAO_DA_EMPRESA_BENEFICIARIA_NO_BANCO),
//                format(item.getControleParticipante(), Campo.NUMERO_CONTROLE_DO_PARTICIPANTE),
//                format(item.getCodigoBanco(), Campo.CODIGO_DO_BANCO_A_SER_DEBITADO_NA_CAMARA_DE_COMPENSACAO),
//                format(item.getCampoMulta(), Campo.CAMPO_DE_MULTA),
//                format(item.getPercentualMulta(), Campo.PERCENTUAL_DE_MULTA),
//                format(item.getNossoNumero(), Campo.IDENTIFICACAO_DO_TITULO_NO_BANCO),
//                format(item.getDigitoConferenciaNumeroBanco(), Campo.DIGITO_DE_AUTO_CONFERENCIA_DO_NUMERO_BANCARIO),
//                format(item.getDescontoDia(), Campo.DESCONTO_BONIFICACAO_POR_DIA),
//                format(item.getCondicaoEmpissaoPapeladaCobranca(), Campo.CONDICAO_PARA_EMISSAO_DA_PAPELETA_DE_COBRANCA),
//                format(item.getBoletoDebitoAutomatico(), Campo.IDENT_SE_EMITE_BOLETO_PARA_DEBITO_AUTOMATICO),
//                format(item.getIdentificacaoOcorrencia(), Campo.IDENTIFICACAO_DA_OPERACAO_DO_BANCO),
//                format(item.getNumeroDocumento(), Campo.NUMERO_DO_DOCUMENTO),
//                format(item.getDataVencimento(), Campo.DATA_DO_VENCIMENTO_DO_TITULO),
//                format(item.getValorTitulo(), Campo.VALOR_DO_TITULO),
//                format(item.getEspecieTitulo(), Campo.ESPECIE_DE_TITULO),
//                format(item.getDataEmissao(), Campo.DATA_DA_EMISSAO_DO_TITULO),
//                format(item.getPrimeiraInstrucao(), Campo.PRIMEIRA_INSTRUCAO),
//                format(item.getSegundaInstrucao(), Campo.SEGUNDA_INSTRUCAO),
//                format(item.getMoraDia(), Campo.VALOR_A_SER_COBRADO_POR_DIA_DE_ATRASO),
//                format(item.getDataLimiteDescontoConcessao(), Campo.DATA_LIMITE_P_CONCESSAO_DE_DESCONTO),
//                format(item.getValorDesconto(), Campo.VALOR_DO_DESCONTO),
//                format(item.getValorIOF(), Campo.VALOR_DO_IOF),
//                format(item.getValorAbatimento(), Campo.VALOR_DO_ABATIMENTO_A_SER_CONCEDIDO_OU_CANCELADO_VALOR_ABATIMENTO),
//                format(item.getTipoPagador(), Campo.NUMERO_INSCRICAO_DO_PAGADOR),
//                format(item.getNomePagador(), Campo.NOME_DO_PAGADOR),
//                format(item.getEndereco(), Campo.ENDERECO_COMPLETO),
//                format(item.getPrimeiraMensagem(), Campo.PRIMEIRA_MENSAGEM),
//                format(item.getCep(), Campo.CEP),
//                format(item.getSufixoCEP(), Campo.SUFIXO_DO_CEP),
//                format(item.getSegundaMensagem(), Campo.SEGUNDA_MENSAGEM),
//                format(item.getSequencialRegistro(), Campo.NUMERO_SEQUENCIAL_DO_REGISTRO),
//                null);


//        return new Cnab400(item.getIdentRegistro(), item.getAgenciaDebito(), item.getDigitoAgencia(), item.getRazaoAgencia(), item.getContaCorrente(), item.getDigitoConta(), item.getIdentBeneficiario(),
//                item.getControleParticipante(), item.getCodigoBanco(), item.getCampoMulta(), item.getPercentualMulta(), item.getNossoNumero(), item.getDigitoConferenciaNumeroBanco(), item.getDescontoDia(),
//                item.getCondicaoEmpissaoPapeladaCobranca(), item.getBoletoDebitoAutomatico(), item.getIdentificacaoOcorrencia(), item.getNumeroDocumento(), item.getDataVencimento(), item.getValorTitulo(),
//                item.getEspecieTitulo(), item.getDataEmissao(), item.getPrimeiraInstrucao(), item.getSegundaInstrucao(), item.getMoraDia(), item.getDataLimiteDescontoConcessao(), item.getValorDesconto(),
//                item.getValorIOF(), item.getValorAbatimento(), item.getTipoPagador(), item.getNomePagador(), item.getEndereco(), item.getPrimeiraMensagem(), item.getCep(), item.getSufixoCEP(), item.getSegundaMensagem(),
//                item.getSequencialRegistro(), item.getLinha());
        return new Cnab400(null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null);

    }

    private String format(Object o, Campo campo) {
        StringBuilder builder = new StringBuilder();
        if (o instanceof String s) {
            if (isNull(s) || s.isBlank()) {
                builder.append(" ".repeat(Math.max(0, campo.getSize())));
            }
            return builder.toString();
        }

        if (o instanceof Integer integer) {
            if (isNull(integer) || integer.equals(0)) {
                builder.append("0".repeat(Math.max(0, campo.getSize())));
            }
            return builder.toString();
        }

        if (o instanceof BigDecimal bd) {
            if (isNull(bd) || bd.equals(BigDecimal.ZERO)) {
                builder.append("0".repeat(Math.max(0, campo.getSize())));
                return builder.toString();
            }
        }

        if (o instanceof Date date) {
            if (isNull(date)) {
                builder.append(" ".repeat(Math.max(0, campo.getSize())));
                return builder.toString();
            }
            SimpleDateFormat dateFormated = new SimpleDateFormat("ddMMyyyy");
            return dateFormated.format(date);
        }

        return o.toString();
    }
}
