package com.batsworks.batch.service;

import com.batsworks.batch.config.cnab.CnabReader;
import com.batsworks.batch.domain.enums.CnabType;
import com.batsworks.batch.domain.enums.Status;
import com.batsworks.batch.domain.records.Cnab400;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.batsworks.batch.config.utils.Utilities.randomName;
import static java.util.Objects.nonNull;

@Service
public class CnabService {

    @Autowired
    private JobLauncher jobLauncherAsync;
    @Autowired
    private JobLauncher asyncWrite;
    @Autowired
    private Job jobCnab;
    @Autowired
    private Job jobWriteCnab;
    @Autowired
    private CnabReader<Cnab400> cnabReader;

    public void uploadCnabFile(MultipartFile file, CnabType tipo) throws Exception {
        var fileName = StringUtils.cleanPath(nonNull(file.getOriginalFilename()) ? file.getOriginalFilename() : randomName());

        var jobParameters = new JobParametersBuilder()
                .addJobParameter("cnab", fileName, String.class, false)
                .toJobParameters();

        cnabReaderConfig(file);
        if (tipo.equals(CnabType.CNAB400))
            jobLauncherAsync.run(jobCnab, jobParameters);
    }

    private void cnabReaderConfig(MultipartFile file) throws IOException {
        cnabReader.setLineMapper(lineMapper());
        cnabReader.setStream(file.getBytes());
        cnabReader.setResource(file.getResource());
        cnabReader.setStrict(false);
        cnabReader.setLinesToSkip(1);
        cnabReader.setName("CUSTOM_CNAB_READER");
    }

    public LineMapper<Cnab400> lineMapper() {
        DefaultLineMapper<Cnab400> lineMapper = new DefaultLineMapper<>();

        FixedLengthTokenizer lineTokenizer = new FixedLengthTokenizer();
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("identRegistro", "agenciaDebito", "digitoAgencia",
                "razaoAgencia", "contaCorrente", "digitoConta",
                "identBeneficiario", "controleParticipante", "codigoBanco",
                "campoMulta", "percentualMulta", "nossoNumero",
                "digitoConferenciaNumeroBanco", "descontoDia", "condicaoEmpissaoPapeladaCobranca",
                "boletoDebitoAutomatico", "identificacaoOcorrencia", "numeroDocumento",
                "dataVencimento", "valorTitulo", "especieTitulo",
                "dataEmissao", "primeiraInstrucao", "segundaInstrucao",
                "moraDia", "dataLimiteDescontoConcessao", "valorDesconto",
                "valorIOF", "valorAbatimento", "tipoPagador",
                "nomePagador", "endereco", "primeiraMensagem",
                "cep", "sufixoCEP", "segundaMensagem", "sequencialRegistro");

        lineTokenizer.setColumns(new Range(1, 1), new Range(2, 6), new Range(7, 7), new Range(8, 12), new Range(13, 19),
                new Range(20, 20), new Range(21, 37), new Range(38, 52), new Range(63, 65), new Range(66, 66),
                new Range(67, 70), new Range(71, 81), new Range(82, 82), new Range(83, 92), new Range(93, 93),
                new Range(94, 94), new Range(109, 110), new Range(111, 120), new Range(121, 126), new Range(127, 139),
                new Range(148, 149), new Range(151, 156), new Range(157, 158), new Range(159, 160), new Range(161, 173),
                new Range(174, 179), new Range(180, 192), new Range(193, 205), new Range(206, 218), new Range(219, 220),
                new Range(235, 274), new Range(275, 314), new Range(315, 326), new Range(327, 331), new Range(332, 334),
                new Range(335, 394), new Range(395, 400));

        lineMapper.setFieldSetMapper(new RecordFieldSetMapper<>(Cnab400.class));
        lineMapper.setLineTokenizer(lineTokenizer);

        return lineMapper;
    }

    public Object downloadCnab() {
        try {
            var jobParameters = new JobParametersBuilder()
                    .addJobParameter("download_cnab", randomName(), String.class, false)
                    .toJobParameters();
            asyncWrite.run(jobWriteCnab, jobParameters);
        } catch (Exception e) {
            System.out.printf("log: {}\n", e.getMessage());
            e.printStackTrace();
        }
        return Status.PROCESSANDO;
    }
}
