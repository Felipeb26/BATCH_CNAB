package com.batsworks.batch.cnab.read;

import com.batsworks.batch.client.ServiceClient;
import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.config.exception.CnabProcessingException;
import com.batsworks.batch.domain.entity.Arquivo;
import com.batsworks.batch.domain.enums.SituacaoCnab;
import com.batsworks.batch.domain.enums.Zones;
import com.batsworks.batch.domain.records.Cnab;
import com.batsworks.batch.domain.records.Cnab400;
import com.batsworks.batch.repository.ArquivoRepository;
import com.batsworks.batch.repository.CnabRepository;
import com.batsworks.batch.service.CnabService;
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
    @Autowired
    private CnabService cnabService;
    private JobParameters map;
    private Optional<Arquivo> arquivo = Optional.empty();
    private Long lastLine;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        map = stepExecution.getJobParameters();
        var path = map.getString("path");
        Long id = map.getLong("id");
        log.info("==========================> START PROCESSING FILE {} AT {}", path, map.getString("time"));
        if (id == null) {
            throw new BussinesException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi Localizado arquivo");
        }
        arquivo = arquivoRepository.findById(id);
        arquivo.ifPresent(value -> lastLine = cnabRepository.findLastByIdArquivo(value.getId()));
    }

    @Override
    public Cnab process(Cnab400 cnab400) throws Exception {
        if (isNull(cnab400.controleParticipante()) || cnab400.controleParticipante().isBlank()) return null;


        if (nonNull(lastLine) && lastLine >= parseLong(cnab400.linha())) {
            return null;
        }

        var dataCadastro = LocalDateTime.now(Zones.AMERIACA_SAO_PAULO.getZone());
        var cnab = new Cnab(null, cnab400.identRegistro(), cnab400.agenciaDebito(), cnab400.digitoAgencia(), cnab400.razaoAgencia(), cnab400.contaCorrente(), cnab400.digitoConta(),
                cnab400.identBeneficiario(), cnab400.controleParticipante(), cnab400.codigoBanco(), cnab400.campoMulta(), cnab400.percentualMulta(), cnab400.nossoNumero(),
                cnab400.digitoConferenciaNumeroBanco(), cnab400.descontoDia(), cnab400.condicaoEmpissaoPapeladaCobranca(), cnab400.boletoDebitoAutomatico(), cnab400.identificacaoOcorrencia(),
                cnab400.numeroDocumento(), null, cnab400.valorTitulo(), cnab400.especieTitulo(), null, cnab400.primeiraInstrucao(), cnab400.segundaInstrucao(),
                cnab400.moraDia(), null, cnab400.valorDesconto(), cnab400.valorIOF(), cnab400.valorAbatimento(), cnab400.tipoPagador(), cnab400.nomePagador(),
                cnab400.endereco(), cnab400.primeiraMensagem(), cnab400.cep(), cnab400.sufixoCEP(), cnab400.segundaMensagem(), cnab400.sequencialRegistro(), Integer.parseInt(cnab400.linha()),
                arquivo.orElse(null), SituacaoCnab.AGUARDANDO_REGISTRO.name(), dataCadastro).withDates(cnab400.dataVencimento(), cnab400.dataEmissao(), cnab400.dataLimiteDescontoConcessao());


        try {
            var salvarBoletoCnab = decisaoPorOcorrencia(cnab400.identificacaoOcorrencia(), cnab);
            if (Boolean.FALSE.equals(salvarBoletoCnab)) return null;
        } catch (CnabProcessingException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CnabProcessingException(e.getMessage(), cnab.linha());
        }

        log.info("==========================> FINISHING PROCESSING LINE {} AT {}", cnab400.linha(), map.getString("time"));
        return cnab;
    }

    private Boolean decisaoPorOcorrencia(Long identificacaoOcorrencia, Cnab cnab) throws Exception {
        var ocorrencia = identificacaoOcorrencia.intValue();
        final ResponseEntity<Object> response;
        log.info("ocorrencia {}\n", ocorrencia);
        Boolean remessa = false;

        switch (ocorrencia) {
            case 1 -> {
                response = serviceClient.findWallById();
                log.info("STATUS CODE {}", response.getStatusCode());
                return true;
            }
            case 2 -> cnabService.ocorrencia02(cnab);
            case 4 -> cnabService.ocorrencia04(cnab);
            case 5 -> cnabService.ocorrencia05(cnab);
            case 6 -> cnabService.ocorrencia06(cnab);
            case 7 -> cnabService.ocorrencia07(cnab);
            case 8 -> cnabService.ocorrencia08(cnab);
            case 20 -> cnabService.ocorrencia20(cnab);
            default ->
                    throw new CnabProcessingException("Ocorrencia %s não reconhecida no sistema".formatted(ocorrencia), cnab.linha());
        }
        return remessa;
    }

}