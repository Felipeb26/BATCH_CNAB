package com.batsworks.batch.config.cnab;

import com.batsworks.batch.domain.records.Cnab;
import com.batsworks.batch.domain.records.Cnab400;
import com.batsworks.batch.database.repository.ArquivoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.isNull;

@Slf4j
public class CnabProcessor implements ItemProcessor<Cnab400, Cnab> {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private ArquivoRepository arquivoRepository;
    private Long id;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        id = stepExecution.getJobParameters().getLong("id");
    }


    @Override
    public Cnab process(Cnab400 cnab) throws Exception {
        if (isNull(cnab.controleParticipante()) || cnab.controleParticipante().isBlank())
            return null;

        var arquivo = arquivoRepository.findById(id);

        return new Cnab(null, cnab.identRegistro(), cnab.agenciaDebito(), cnab.digitoAgencia(), cnab.razaoAgencia(), cnab.contaCorrente(), cnab.digitoConta(), cnab.identBeneficiario(),
                cnab.controleParticipante(), cnab.codigoBanco(), cnab.campoMulta(), cnab.percentualMulta(), cnab.nossoNumero(), cnab.digitoConferenciaNumeroBanco(),
                cnab.descontoDia(), cnab.condicaoEmpissaoPapeladaCobranca(), cnab.boletoDebitoAutomatico(), cnab.identificacaoOcorrencia(), cnab.numeroDocumento(),
                null, cnab.valorTitulo(), cnab.especieTitulo(), null, cnab.primeiraInstrucao(), cnab.segundaInstrucao(), cnab.moraDia(),
                null, cnab.valorDesconto(), cnab.valorIOF(), cnab.valorAbatimento(), cnab.tipoPagador(), cnab.nomePagador(), cnab.endereco(),
                cnab.primeiraMensagem(), cnab.cep(), cnab.sufixoCEP(), cnab.segundaMensagem(), cnab.sequencialRegistro(), arquivo.orElse(null)
        ).withDates(cnab.dataVencimento(), cnab.dataEmissao(), cnab.dataLimiteDescontoConcessao());
    }

}