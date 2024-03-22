package com.batsworks.batch.cnab.write;

import com.batsworks.batch.domain.entity.CnabEntity;
import com.batsworks.batch.domain.records.CnabWritter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.Objects.isNull;

@Slf4j
public class CnabWriteProcessor implements ItemProcessor<CnabEntity, CnabWritter> {

    @Override
    public CnabWritter process(CnabEntity item) {
        return new CnabWritter(
                format(item.getIdentRegistro(), Campo.IDENTIFICACAO_DO_REGISTRO),
                format(item.getAgenciaDebito(), Campo.AGENCIA_DE_DEBITO),
                format(item.getDigitoAgencia(), Campo.DIGITO_DA_AGENCIA_DE_DEBITO),
                format(item.getRazaoAgencia(), Campo.RAZAO_DA_CONTA_CORRENTE),
                format(item.getContaCorrente(), Campo.CONTA_CORRENTE),
                format(item.getDigitoConta(), Campo.DIGITO_DA_CONTA_CORRENTE),
                format(item.getIdentBeneficiario(), Campo.IDENTIFICACAO_DA_EMPRESA_BENEFICIARIA_NO_BANCO),
                format(item.getControleParticipante(), Campo.NUMERO_CONTROLE_DO_PARTICIPANTE),
                format(item.getCodigoBanco(), Campo.CODIGO_DO_BANCO_A_SER_DEBITADO_NA_CAMARA_DE_COMPENSACAO),
                format(item.getCampoMulta(), Campo.CAMPO_DE_MULTA),
                format(item.getPercentualMulta(), Campo.PERCENTUAL_DE_MULTA),
                format(item.getNossoNumero(), Campo.IDENTIFICACAO_DO_TITULO_NO_BANCO),
                format(item.getDigitoConferenciaNumeroBanco(), Campo.DIGITO_DE_AUTO_CONFERENCIA_DO_NUMERO_BANCARIO),
                format(item.getDescontoDia(), Campo.DESCONTO_BONIFICACAO_POR_DIA),
                format(item.getCondicaoEmpissaoPapeladaCobranca(), Campo.CONDICAO_PARA_EMISSAO_DA_PAPELETA_DE_COBRANCA),
                format(item.getBoletoDebitoAutomatico(), Campo.IDENT_SE_EMITE_BOLETO_PARA_DEBITO_AUTOMATICO),
                format(item.getIdentificacaoOcorrencia(), Campo.IDENTIFICACAO_DA_OCORRENCIA),
                format(item.getNumeroDocumento(), Campo.NUMERO_DO_DOCUMENTO),
                format(item.getDataVencimento(), Campo.DATA_DO_VENCIMENTO_DO_TITULO),
                format(item.getValorTitulo(), Campo.VALOR_DO_TITULO),
                format(item.getEspecieTitulo(), Campo.ESPECIE_DE_TITULO),
                format(item.getDataEmissao(), Campo.DATA_DA_EMISSAO_DO_TITULO),
                format(item.getPrimeiraInstrucao(), Campo.PRIMEIRA_INSTRUCAO),
                format(item.getSegundaInstrucao(), Campo.SEGUNDA_INSTRUCAO),
                format(item.getMoraDia(), Campo.VALOR_A_SER_COBRADO_POR_DIA_DE_ATRASO),
                format(item.getDataLimiteDescontoConcessao(), Campo.DATA_LIMITE_P_CONCESSAO_DE_DESCONTO),
                format(item.getValorDesconto(), Campo.VALOR_DO_DESCONTO),
                format(item.getValorIOF(), Campo.VALOR_DO_IOF),
                format(item.getValorAbatimento(), Campo.VALOR_DO_ABATIMENTO_A_SER_CONCEDIDO_OU_CANCELADO_VALOR_ABATIMENTO),
                format(item.getTipoPagador(), Campo.IDENTIFICACAO_DO_TIPO_DE_INSCRICAO_DO_PAGADOR),
                format(item.getNomePagador(), Campo.NOME_DO_PAGADOR),
                format(item.getEndereco(), Campo.ENDERECO_COMPLETO),
                format(item.getPrimeiraMensagem(), Campo.PRIMEIRA_MENSAGEM),
                format(item.getCep(), Campo.CEP),
                format(item.getSufixoCEP(), Campo.SUFIXO_DO_CEP),
                format(null, Campo.BRANCOS),
                format(null, Campo.BRANCOS),
                format(item.getSegundaMensagem(), Campo.SEGUNDA_MENSAGEM),
                format(null, Campo.BRANCOS),
                format(item.getSequencialRegistro(), Campo.NUMERO_SEQUENCIAL_DO_REGISTRO));

    }

    private String format(Object o, Campo campo) {
        StringBuilder builder = new StringBuilder();
        if (isNull(o))
            builder.append(" ".repeat(Math.max(0, campo.getSize())));

        if (o instanceof Date date) {
            SimpleDateFormat dateFormated = new SimpleDateFormat("ddMMyyyy");
            return dateFormated.format(date);
        } else {
            builder.append(o);
            return builder.toString();
        }
    }
}
