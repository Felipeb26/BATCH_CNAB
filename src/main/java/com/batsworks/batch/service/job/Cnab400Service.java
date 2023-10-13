package com.batsworks.batch.service.job;

import com.batsworks.batch.config.cnab.CnabProcessor;
import com.batsworks.batch.config.cnab.CnabReader;
import com.batsworks.batch.domain.records.Cnab;
import com.batsworks.batch.domain.records.Cnab400;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class Cnab400Service {

    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository repository;

    @Bean
    Job jobCnab(Step step, JobRepository jobRepository) {
        return new JobBuilder("CNAB_400_JOB", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    Step stepCnab(ItemReader<Cnab400> reader, ItemProcessor<Cnab400, Cnab> processor, ItemWriter<Cnab> writer) {
        return new StepBuilder("CNAB_400_STEP", repository)
                .<Cnab400, Cnab>chunk(20, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    FlatFileItemReader<Cnab400> readerCnab(@Value("#{jobParameters['cnabFile']}") Resource resource, LineMapper<Cnab400> lineMapper) {
        return new FlatFileItemReaderBuilder<Cnab400>()
                .name("CNAB_400_READER")
                .resource(resource)
                .linesToSkip(1)
                .fixedLength()
                .strict(false)
                .columns(new Range(1, 1), new Range(2, 6), new Range(7, 7), new Range(8, 12), new Range(13, 19),
                        new Range(20, 20), new Range(21, 37), new Range(38, 52), new Range(63, 65), new Range(66, 66),
                        new Range(67, 70), new Range(71, 81), new Range(82, 82), new Range(83, 92), new Range(93, 93),
                        new Range(94, 94), new Range(109, 110), new Range(111, 120), new Range(121, 126), new Range(127, 139),
                        new Range(148, 149), new Range(151, 156), new Range(157, 158), new Range(159, 160), new Range(161, 173),
                        new Range(174, 179), new Range(180, 192), new Range(193, 205), new Range(206, 218), new Range(219, 220), new Range(235, 274),
                        new Range(275, 314), new Range(315, 326), new Range(327, 331), new Range(332, 334), new Range(335, 394), new Range(395, 400))
                .names("identRegistro", "agenciaDebito", "digitoAgencia", "razaoAgencia", "contaCorrente", "digitoConta",
                        "identBeneficiario", "controleParticipante", "codigoBanco", "campoMulta", "percentualMulta",
                        "nossoNumero", "digitoConferenciaNumeroBanco", "descontoDia", "condicaoEmpissaoPapeladaCobranca",
                        "boletoDebitoAutomatico", "identificacaoOcorrencia", "numeroDocumento", "dataVencimento",
                        "valorTitulo", "especieTitulo", "dataEmissao", "primeiraInstrucao", "segundaInstrucao",
                        "moraDia", "dataLimiteDescontoConcessao", "valorDesconto", "valorIOF", "valorAbatimento",
                        "tipoPagador", "nomePagador", "endereco", "primeiraMensagem", "cep", "sufixoCEP", "segundaMensagem",
                        "sequencialRegistro")
                .targetType(Cnab400.class)
                .build();
    }

    @Bean
    CnabProcessor processor() {
        return new CnabProcessor();
    }

    @Bean
    JdbcBatchItemWriter<Cnab> writerCnab(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Cnab>()
                .dataSource(dataSource)
                .sql("INSERT INTO cnab(identRegistro, agenciaDebito, digitoAgencia,razaoAgencia, contaCorrente, digitoConta, identBeneficiario,controleParticipante," +
                        " codigoBanco, campoMulta, percentualMulta, nossoNumero, digitoConferenciaNumeroBanco,descontoDia, condicaoEmpissaoPapeladaCobranca," +
                        " boletoDebitoAutomatico,identificacaoOcorrencia, numeroDocumento,dataVencimento, valorTitulo, especieTitulo, dataEmissao, primeiraInstrucao," +
                        " segundaInstrucao, moraDia,dataLimiteDescontoConcessao, valorDesconto, valorIOF, valorAbatimento, tipoPagador, nomePagador, endereco,primeiraMensagem," +
                        " cep, sufixoCEP, segundaMensagem, sequencialRegistro) VALUES (:identRegistro,:agenciaDebito,:digitoAgencia,:razaoAgencia,:contaCorrente," +
                        ":digitoConta,:identBeneficiario,:controleParticipante,:codigoBanco,:campoMulta,:percentualMulta,:nossoNumero,:digitoConferenciaNumeroBanco," +
                        ":descontoDia,:condicaoEmpissaoPapeladaCobranca,:boletoDebitoAutomatico,:identificacaoOcorrencia,:numeroDocumento,:dataVencimento,:valorTitulo," +
                        ":especieTitulo,:dataEmissao,:primeiraInstrucao,:segundaInstrucao,:moraDia,:dataLimiteDescontoConcessao,:valorDesconto,:valorIOF,:valorAbatimento," +
                        ":tipoPagador,:nomePagador,:endereco,:primeiraMensagem,:cep,:sufixoCEP,:segundaMensagem,:sequencialRegistro)")
                .beanMapped().build();
    }

    @Bean
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

    @Bean
    JobLauncher jobLauncherAsync(JobRepository jobRepository) throws Exception {
        var jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }

    @Bean
    @Primary
    CnabReader<Cnab400> cnabReader(LineMapper<Cnab400> lineMapper) {
        var cnab = new CnabReader<Cnab400>();
        cnab.setLineMapper(lineMapper);
        return cnab;
    }

}
