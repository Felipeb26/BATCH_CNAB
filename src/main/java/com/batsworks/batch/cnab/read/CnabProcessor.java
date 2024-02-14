package com.batsworks.batch.cnab.read;

import com.batsworks.batch.client.ServiceClient;
import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.config.exception.CnabException;
import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.Zones;
import com.batsworks.batch.domain.records.Cnab;
import com.batsworks.batch.domain.records.Cnab400;
import com.batsworks.batch.repository.ArquivoRepository;
import com.batsworks.batch.repository.CnabRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.batsworks.batch.utils.Files.resolveFileName;
import static java.lang.Long.parseLong;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
public class CnabProcessor implements ItemProcessor<Cnab400, Cnab> {

    @Autowired
    private ArquivoRepository arquivoRepository;
    @Autowired
    private CnabRepository cnabRepository;
    @Autowired
    private ServiceClient serviceClient;
    private JobParameters map;
    private Optional<Arquivo> arquivo = Optional.empty();
    private Long lastLine;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        map = stepExecution.getJobParameters();
        var path = map.getString("path");
        path = resolveFileName(path, true);
        Long id = map.getLong("id");
        log.info("==========================> START PROCESSING FILE {} AT {}", path, map.getString("time"));
        if(id == null){
            throw new BussinesException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi Localizado arquivo");
        }
        arquivo = arquivoRepository.findById(id);
        arquivo.ifPresent(value -> lastLine = cnabRepository.findLastByIdArquivo(value.getId()));
    }

    @Override
    public Cnab process(Cnab400 cnab) throws Exception {
        if (isNull(cnab.controleParticipante()) || cnab.controleParticipante().isBlank()) return null;


        if (nonNull(lastLine) && lastLine >= parseLong(cnab.linha())) {
            return null;
        }

        decisaoPorOcorrencia(cnab.identificacaoOcorrencia());

        final ResponseEntity<Object> response;
        try {
            response = serviceClient.findWallById();
            log.info("STATUS CODE {}", response.getStatusCode());
        } catch (BussinesException bussines) {
            throw new CnabException(bussines.getMessage(), Integer.parseInt(cnab.linha()));
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        log.info("==========================> FINISHING PROCESSING LINE {} AT {}", cnab.linha(), map.getString("time"));
        var dataCadastro = LocalDateTime.now(Zones.AMERIACA_SAO_PAULO.getZone());
        return new Cnab(null, cnab.identRegistro(), cnab.agenciaDebito(), cnab.digitoAgencia(), cnab.razaoAgencia(), cnab.contaCorrente(), cnab.digitoConta(), cnab.identBeneficiario(),
                cnab.controleParticipante(), cnab.codigoBanco(), cnab.campoMulta(), cnab.percentualMulta(), cnab.nossoNumero(), cnab.digitoConferenciaNumeroBanco(),
                cnab.descontoDia(), cnab.condicaoEmpissaoPapeladaCobranca(), cnab.boletoDebitoAutomatico(), cnab.identificacaoOcorrencia(), cnab.numeroDocumento(),
                null, cnab.valorTitulo(), cnab.especieTitulo(), null, cnab.primeiraInstrucao(), cnab.segundaInstrucao(), cnab.moraDia(),
                null, cnab.valorDesconto(), cnab.valorIOF(), cnab.valorAbatimento(), cnab.tipoPagador(), cnab.nomePagador(), cnab.endereco(),
                cnab.primeiraMensagem(), cnab.cep(), cnab.sufixoCEP(), cnab.segundaMensagem(), cnab.sequencialRegistro(), Integer.parseInt(cnab.linha()), arquivo.orElse(null), dataCadastro
        ).withDates(cnab.dataVencimento(), cnab.dataEmissao(), cnab.dataLimiteDescontoConcessao());
    }

    private void decisaoPorOcorrencia(Long identificacaoOcorrencia) {
        var ocorrencia = identificacaoOcorrencia.intValue();
        switch (ocorrencia) {
            case 1 -> log.info("ocorrencia remessa {}", ocorrencia);
            case 2 -> log.info("ocorrencia {}", ocorrencia);
            case 3 -> log.info("ocorrencia {}", ocorrencia);
            case 4 -> log.info("ocorrencia {}", ocorrencia);
            case 5 -> log.info("ocorrencia {}", ocorrencia);
            case 6 -> log.info("ocorrencia {}", ocorrencia);
            default -> log.info("remessa");
        }
    }

}