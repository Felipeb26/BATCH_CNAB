package com.batsworks.batch.cnab.read;

import com.batsworks.batch.config.exception.CnabException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;

public class CnabLineMapper<Cnab400> extends DefaultLineMapper<Cnab400> {
    @Override
    public Cnab400 mapLine(String line, int lineNumber) throws Exception {
        try {
            if (line.length() > 400)
                throw new CnabException("Titulo %s com Ocorrencia %s".formatted(line.substring(70, 81), line.substring(108, 110)), lineNumber, line);
            com.batsworks.batch.domain.records.Cnab400 cnab = (com.batsworks.batch.domain.records.Cnab400) super.mapLine(line, lineNumber);

            return (Cnab400) cnabSaveLine(cnab, lineNumber);
        } catch (Exception e) {
            throw new CnabException(e.getMessage(), lineNumber, line);
        }
    }

    private com.batsworks.batch.domain.records.Cnab400 cnabSaveLine(com.batsworks.batch.domain.records.Cnab400 cnab, int line) {
        return new com.batsworks.batch.domain.records.Cnab400(cnab.identRegistro(), cnab.agenciaDebito(), cnab.digitoAgencia(), cnab.razaoAgencia(), cnab.contaCorrente(), cnab.digitoConta(), cnab.identBeneficiario(), cnab.controleParticipante(), cnab.codigoBanco(), cnab.campoMulta(), cnab.percentualMulta(),
                cnab.nossoNumero(), cnab.digitoConferenciaNumeroBanco(), cnab.descontoDia(), cnab.condicaoEmpissaoPapeladaCobranca(), cnab.boletoDebitoAutomatico(), cnab.identificacaoOcorrencia(), cnab.numeroDocumento(),
                cnab.dataVencimento(), cnab.valorTitulo(), cnab.especieTitulo(), cnab.dataEmissao(), cnab.primeiraInstrucao(),
                cnab.segundaInstrucao(), cnab.moraDia(), cnab.dataLimiteDescontoConcessao(),cnab.valorDesconto(), cnab.valorIOF(),
                cnab.valorAbatimento(), cnab.tipoPagador(), cnab.nomePagador(), cnab.endereco(), cnab.primeiraMensagem(), cnab.cep(),
                cnab.sufixoCEP(), cnab.segundaMensagem(), cnab.sequencialRegistro(), "0").withLine(line);
    }
}