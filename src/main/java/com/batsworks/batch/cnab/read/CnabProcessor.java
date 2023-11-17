package com.batsworks.batch.cnab.read;

import com.batsworks.batch.domain.enums.Zones;
import com.batsworks.batch.domain.records.Cnab;
import com.batsworks.batch.domain.records.Cnab400;
import com.batsworks.batch.repository.ArquivoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.batsworks.batch.utils.Utilities.resolveFileName;
import static java.util.Objects.isNull;

@Slf4j
public class CnabProcessor implements ItemProcessor<Cnab400, Cnab> {

    @Autowired
    private ArquivoRepository arquivoRepository;
    private Long id;
    private JobParameters map;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        map = stepExecution.getJobParameters();
        var path = map.getString("path");
        path = resolveFileName(path, true);
        id = map.getLong("id");
        log.info("==========================> START PROCESSING FILE {} AT {}", path, map.getString("time"));
    }

    @Override
    public Cnab process(Cnab400 cnab) throws Exception {
        if (isNull(cnab.controleParticipante()) || cnab.controleParticipante().isBlank()) return null;

        var arquivo = arquivoRepository.findById(id);
        log.info("==========================> START PROCESSING FILE AT {}", map.getString("time"));

        decisaoPorOcorrencia(cnab.identificacaoOcorrencia());


        var dataCadastro = LocalDateTime.now(Zones.AMERIACA_SAO_PAULO.getZone());
        return new Cnab(null, cnab.identRegistro(), cnab.agenciaDebito(), cnab.digitoAgencia(), cnab.razaoAgencia(), cnab.contaCorrente(), cnab.digitoConta(), cnab.identBeneficiario(),
                cnab.controleParticipante(), cnab.codigoBanco(), cnab.campoMulta(), cnab.percentualMulta(), cnab.nossoNumero(), cnab.digitoConferenciaNumeroBanco(),
                cnab.descontoDia(), cnab.condicaoEmpissaoPapeladaCobranca(), cnab.boletoDebitoAutomatico(), cnab.identificacaoOcorrencia(), cnab.numeroDocumento(),
                null, cnab.valorTitulo(), cnab.especieTitulo(), null, cnab.primeiraInstrucao(), cnab.segundaInstrucao(), cnab.moraDia(),
                null, cnab.valorDesconto(), cnab.valorIOF(), cnab.valorAbatimento(), cnab.tipoPagador(), cnab.nomePagador(), cnab.endereco(),
                cnab.primeiraMensagem(), cnab.cep(), cnab.sufixoCEP(), cnab.segundaMensagem(), cnab.sequencialRegistro(), arquivo.orElse(null), dataCadastro
        ).withDates(cnab.dataVencimento(), cnab.dataEmissao(), cnab.dataLimiteDescontoConcessao());
    }

    private void decisaoPorOcorrencia(Long identificacaoOcorrencia){
        var ocorrencia = identificacaoOcorrencia.intValue();
        switch (ocorrencia){
            case 2 -> log.info("ocorrencia 02");
            case 3 -> log.info("ocorrencia 03");
            case 4 -> log.info("ocorrencia 04");
            case 5 -> log.info("ocorrencia 05");
            default -> log.info("remessa");
        }
    }

}