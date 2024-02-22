package com.batsworks.batch.cnab.read;

import com.batsworks.batch.config.exception.CnabProcessingException;
import com.batsworks.batch.domain.records.Cnab400;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;

@SuppressWarnings(value = "unchecked")
public class CnabLineMapper<T> extends DefaultLineMapper<T> {

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        try {
            if (isHeader(line) || Boolean.FALSE.equals(line.length() == 400)) {
                throw new CnabProcessingException("Titulo %s com %s caracteres".formatted(line.substring(70, 81), line.length()), lineNumber, line);
            }
            var mappedLine = super.mapLine(line, lineNumber);
            if (mappedLine instanceof Cnab400 cnab)
                return (T) cnabSaveLine(cnab, lineNumber);
            return mappedLine;
        } catch (Exception e) {
            throw new CnabProcessingException(e.getMessage(), lineNumber, line);
        }
    }

    private Boolean isHeader(String line) {
        return line.startsWith("01");
    }

    private Cnab400 cnabSaveLine(Cnab400 cnab, int line) {
        return new Cnab400(cnab.identRegistro(), cnab.agenciaDebito(), cnab.digitoAgencia(), cnab.razaoAgencia(), cnab.contaCorrente(), cnab.digitoConta(), cnab.identBeneficiario(), cnab.controleParticipante(), cnab.codigoBanco(), cnab.campoMulta(), cnab.percentualMulta(),
                cnab.nossoNumero(), cnab.digitoConferenciaNumeroBanco(), cnab.descontoDia(), cnab.condicaoEmpissaoPapeladaCobranca(), cnab.boletoDebitoAutomatico(), cnab.identificacaoOcorrencia(), cnab.numeroDocumento(),
                cnab.dataVencimento(), cnab.valorTitulo(), cnab.especieTitulo(), cnab.dataEmissao(), cnab.primeiraInstrucao(),
                cnab.segundaInstrucao(), cnab.moraDia(), cnab.dataLimiteDescontoConcessao(), cnab.valorDesconto(), cnab.valorIOF(),
                cnab.valorAbatimento(), cnab.tipoPagador(), cnab.nomePagador(), cnab.endereco(), cnab.primeiraMensagem(), cnab.cep(),
                cnab.sufixoCEP(), cnab.segundaMensagem(), cnab.sequencialRegistro(), "0").withLine(line);
    }
}