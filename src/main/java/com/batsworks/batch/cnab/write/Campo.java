package com.batsworks.batch.cnab.write;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Campo {

    IDENTIFICACAO_DO_REGISTRO("Identificação_do_Registro", 1),
    AGENCIA_DE_DEBITO("Agência_de_Débito", 5),
    DIGITO_DA_AGENCIA_DE_DEBITO("Dígito_da_Agência_de_Débito", 1),
    RAZAO_DA_CONTA_CORRENTE("Razão_da_Conta_Corrente", 5),
    CONTA_CORRENTE("Conta_Corrente", 7),
    DIGITO_DA_CONTA_CORRENTE("Dígito_da_Conta_Corrente", 1),
    IDENTIFICACAO_DA_EMPRESA_BENEFICIARIA_NO_BANCO("Identificação_da_Empresa_Beneficiária_no_Banco", 17),
    NUMERO_CONTROLE_DO_PARTICIPANTE("Nº_Controle_do_Participante", 15),
    BRANCOS("Brancos", 10),
    CODIGO_DO_BANCO_A_SER_DEBITADO_NA_CAMARA_DE_COMPENSACAO("Código_do_Banco_a_ser_debitado_na_Câmara_de_Compensação", 3),
    CAMPO_DE_MULTA("Campo_de_Multa", 1),
    PERCENTUAL_DE_MULTA("Percentual_de_multa", 4),
    IDENTIFICACAO_DO_TITULO_NO_BANCO("Identificação_do_Título_no_Banco", 11),
    DIGITO_DE_AUTO_CONFERENCIA_DO_NUMERO_BANCARIO("Digito_de_Auto_Conferencia_do_Número_Bancário.", 1),
    DESCONTO_BONIFICACAO_POR_DIA("Desconto_Bonificação_por_dia", 10),
    CONDICAO_PARA_EMISSAO_DA_PAPELETA_DE_COBRANCA("Condição_para_Emissão_da_Papeleta_de_Cobrança", 1),
    IDENT_SE_EMITE_BOLETO_PARA_DEBITO_AUTOMATICO("Ident._se_emite_Boleto_para_Débito_Automático", 1),
    IDENTIFICACAO_DA_OPERACAO_DO_BANCO("Identificação_da_Operação_do_Banco", 10),
    INDICADOR_RATEIO_CREDITO("Indicador_Rateio_Crédito", 1),
    ENDERECAMENTO_PARA_AVISO_DO_DEBITO_AUTOMATICO_EM_CONTA_CORRENTE("Endereçamento_para_Aviso_do_Débito_Automático_em_Conta_Corrente", 1),
    QUANTIDADE_DE_PAGAMENTOS("Quantidade_de_pagamentos", 2),
    IDENTIFICACAO_DA_OCORRENCIA("Identificação_da_ocorrência", 2),
    NUMERO_DO_DOCUMENTO("Nº_do_Documento", 10),
    DATA_DO_VENCIMENTO_DO_TITULO("Data_do_Vencimento_do_Título", 6),
    VALOR_DO_TITULO("Valor_do_Título", 13),
    BANCO_ENCARREGADO_DA_COBRANCA("Banco_Encarregado_da_Cobrança", 3),
    AGENCIA_DEPOSITARIA("Agência_Depositária", 5),
    ESPECIE_DE_TITULO("Espécie_de_Título", 2),
    IDENTIFICACAO("Identificação", 1),
    DATA_DA_EMISSAO_DO_TITULO("Data_da_emissão_do_Título", 6),
    PRIMEIRA_INSTRUCAO("1ª_instrução", 2),
    SEGUNDA_INSTRUCAO("2ª_instrução", 2),
    VALOR_A_SER_COBRADO_POR_DIA_DE_ATRASO("Valor_a_ser_cobrado_por_Dia_de_Atraso", 13),
    DATA_LIMITE_P_CONCESSAO_DE_DESCONTO("Data_Limite_P/Concessão_de_Desconto", 6),
    VALOR_DO_DESCONTO("Valor_do_Desconto", 13),
    VALOR_DO_IOF("Valor_do_IOF", 13),
    VALOR_DO_ABATIMENTO_A_SER_CONCEDIDO_OU_CANCELADO_VALOR_ABATIMENTO("Valor_do_Abatimento_a_ser_concedido_ou_cancelado_Valor_Abatimento", 13),
    IDENTIFICACAO_DO_TIPO_DE_INSCRICAO_DO_PAGADOR("Identificação_do_Tipo_de_Inscrição_do_Pagador", 2),
    NUMERO_INSCRICAO_DO_PAGADOR("Nº_Inscrição_do_Pagador", 14),
    NOME_DO_PAGADOR("Nome_do_Pagador", 40),
    ENDERECO_COMPLETO("Endereço_Completo", 40),
    PRIMEIRA_MENSAGEM("1ª_Mensagem", 12),
    CEP("CEP", 5),
    SUFIXO_DO_CEP("Sufixo_do_CEP", 3),
    SEGUNDA_MENSAGEM("2ª_Mensagem", 60),
    NUMERO_SEQUENCIAL_DO_REGISTRO("Nº_Seqüencial_do_Registro", 6);

    private final String name;
    private final int size;

}
