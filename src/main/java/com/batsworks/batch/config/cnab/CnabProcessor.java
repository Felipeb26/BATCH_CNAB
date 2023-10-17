package com.batsworks.batch.config.cnab;

import com.batsworks.batch.domain.entity.ViaCep;
import com.batsworks.batch.domain.records.Cnab;
import com.batsworks.batch.domain.records.Cnab400;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.isNull;

@Slf4j
public class CnabProcessor implements ItemProcessor<Cnab400, Cnab> {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Cnab process(Cnab400 cnab) throws Exception {
        if (isNull(cnab.controleParticipante()) || cnab.controleParticipante().isBlank())
            return null;

//        log.info("CEP: {}-{}, TITULO: {}", cnab.cep(), cnab.sufixoCEP(), cnab.numeroDocumento());
//        var template = restTemplate.getForEntity("https://viacep.com.br/ws/" + cnab.cep() + cnab.sufixoCEP() + "/json/", ViaCep.class);
        return new Cnab(null, cnab.identRegistro(), cnab.agenciaDebito(), cnab.digitoAgencia(), cnab.razaoAgencia(), cnab.contaCorrente(), cnab.digitoConta(), cnab.identBeneficiario(),
                cnab.controleParticipante(), cnab.codigoBanco(), cnab.campoMulta(), cnab.percentualMulta(), cnab.nossoNumero(), cnab.digitoConferenciaNumeroBanco(),
                cnab.descontoDia(), cnab.condicaoEmpissaoPapeladaCobranca(), cnab.boletoDebitoAutomatico(), cnab.identificacaoOcorrencia(), cnab.numeroDocumento(),
                null, cnab.valorTitulo(), cnab.especieTitulo(), null, cnab.primeiraInstrucao(), cnab.segundaInstrucao(), cnab.moraDia(),
                null, cnab.valorDesconto(), cnab.valorIOF(), cnab.valorAbatimento(), cnab.tipoPagador(), cnab.nomePagador(), cnab.endereco(),
                cnab.primeiraMensagem(), cnab.cep(), cnab.sufixoCEP(), cnab.segundaMensagem(), cnab.sequencialRegistro()
        ).withDates(cnab.dataVencimento(), cnab.dataEmissao(), cnab.dataLimiteDescontoConcessao());
    }
}
