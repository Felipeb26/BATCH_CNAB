package com.batsworks.batch.cnab.read;

import com.batsworks.batch.client.ServiceClient;
import com.batsworks.batch.config.exception.BussinesException;
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
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static com.batsworks.batch.config.exception.StatusEnum.UNKNOW_ERROR;
import static com.batsworks.batch.utils.Files.resolveFileName;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
public class CnabProcessor implements ItemProcessor<Cnab400, Cnab> {

    @Autowired
    private ArquivoRepository arquivoRepository;
    @Autowired
    private ServiceClient serviceClient;
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

        decisaoPorOcorrencia(cnab.identificacaoOcorrencia());

        ResponseEntity<Object> response = null;
        try {
            response = serviceClient.findWallById(2L);
        } catch (BussinesException bussines) {
            throw new BussinesException(bussines.getStatusCode(), bussines.getMessage(), bussines.getStatusEnum());
        } catch (Exception e) {
            throw new BussinesException(INTERNAL_SERVER_ERROR, e.getMessage(), UNKNOW_ERROR);
        }

        log.info("==========================> FINISHING PROCESSING LINE AT {}", map.getString("time"));
        var dataCadastro = LocalDateTime.now(Zones.AMERIACA_SAO_PAULO.getZone());
        return new Cnab(null, cnab.identRegistro(), cnab.agenciaDebito(), cnab.digitoAgencia(), cnab.razaoAgencia(), cnab.contaCorrente(), cnab.digitoConta(), cnab.identBeneficiario(),
                cnab.controleParticipante(), cnab.codigoBanco(), cnab.campoMulta(), cnab.percentualMulta(), cnab.nossoNumero(), cnab.digitoConferenciaNumeroBanco(),
                cnab.descontoDia(), cnab.condicaoEmpissaoPapeladaCobranca(), cnab.boletoDebitoAutomatico(), cnab.identificacaoOcorrencia(), cnab.numeroDocumento(),
                null, cnab.valorTitulo(), cnab.especieTitulo(), null, cnab.primeiraInstrucao(), cnab.segundaInstrucao(), cnab.moraDia(),
                null, cnab.valorDesconto(), cnab.valorIOF(), cnab.valorAbatimento(), cnab.tipoPagador(), cnab.nomePagador(), cnab.endereco(),
                cnab.primeiraMensagem(), cnab.cep(), cnab.sufixoCEP(), cnab.segundaMensagem(), cnab.sequencialRegistro(), arquivo.orElse(null), dataCadastro
        ).withDates(cnab.dataVencimento(), cnab.dataEmissao(), cnab.dataLimiteDescontoConcessao());
    }

    private void decisaoPorOcorrencia(Long identificacaoOcorrencia) {
        var ocorrencia = identificacaoOcorrencia.intValue();
        switch (ocorrencia) {
            case 2 -> log.info("ocorrencia 02");
            case 3 -> log.info("ocorrencia 03");
            case 4 -> log.info("ocorrencia 04");
            case 5 -> log.info("ocorrencia 05");
            default -> log.info("remessa");
        }
    }

}