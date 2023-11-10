package com.batsworks.batch.service.job;

import com.batsworks.batch.config.cnab.*;
import com.batsworks.batch.domain.records.Cnab;
import com.batsworks.batch.domain.records.Cnab400;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static com.batsworks.batch.config.utils.Utilities.actualDateString;
import static com.batsworks.batch.config.utils.Utilities.resolveFileName;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class Cnab400Service {

    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository repository;

    @Bean()
    Job jobCnab(Step step, JobRepository jobRepository, CnabSkipListenner cnabSkipListenner) {
        return new JobBuilder("CNAB_400_JOB", jobRepository)
                .listener(cnabSkipListenner)
                .flow(step)
                .next(updateSituacaoCnab())
                .end()
                .build();
    }

    @Bean
    Step step(CnabReader<Cnab400> cnabReader, ItemWriter<Cnab> writerCnab, SkipPolicy skipPolicy) {
        return new StepBuilder("CNAB_400_MINOR_STEP", repository)
                .<Cnab400, Future<Cnab>>chunk(500, platformTransactionManager)
                .allowStartIfComplete(true)
                .reader(cnabReader)
                .processor(asyncItemProcessor())
                .writer(asyncItemWriter(writerCnab))
                .faultTolerant()
                .skipPolicy(skipPolicy)
                .listener(processor())
                .build();
    }

    @Bean
    Step updateSituacaoCnab() {
        return new StepBuilder("CNAB_400_ATUALIZACAO_ARQUIVO", repository)
                .tasklet(cnabTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    CnabTasklet cnabTasklet() {
        return new CnabTasklet();
    }

    @Bean
    @StepScope
    CnabReader<Cnab400> cnabReader(@Value("#{jobParameters['path']}") String resource) {
        resource = resolveFileName(resource, false);
        var cnab = new CnabReader<Cnab400>();
        cnab.setStrict(false);
        cnab.setResource(new PathResource(resource));
        cnab.setName("CUSTOM_CNAB_READER");
        cnab.setLineMapper(lineMapper());
        return cnab;
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
                        " cep, sufixoCEP, segundaMensagem, sequencialRegistro,idArquivo,dataCadastro) VALUES (:identRegistro,:agenciaDebito,:digitoAgencia,:razaoAgencia,:contaCorrente," +
                        ":digitoConta,:identBeneficiario,:controleParticipante,:codigoBanco,:campoMulta,:percentualMulta,:nossoNumero,:digitoConferenciaNumeroBanco," +
                        ":descontoDia,:condicaoEmpissaoPapeladaCobranca,:boletoDebitoAutomatico,:identificacaoOcorrencia,:numeroDocumento,:dataVencimento,:valorTitulo," +
                        ":especieTitulo,:dataEmissao,:primeiraInstrucao,:segundaInstrucao,:moraDia,:dataLimiteDescontoConcessao,:valorDesconto,:valorIOF,:valorAbatimento," +
                        ":tipoPagador,:nomePagador,:endereco,:primeiraMensagem,:cep,:sufixoCEP,:segundaMensagem,:sequencialRegistro,:arquivo.id,:dataCadastro)")
                .beanMapped().build();
    }


    @Bean
    public LineMapper<Cnab400> lineMapper() {
        DefaultLineMapper<Cnab400> lineMapper = new CnabLineMapper<>();

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

    /**
     * JobLauncher é chamado pelo endpoint de forma assyncrona
     * TaskExecutor executa de forma asycrona - multithread
     **/
    @Bean
    JobLauncher jobLauncherAsync(JobRepository repository) throws Exception {
        var jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(repository);
        jobLauncher.setTaskExecutor(taskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("BATSWORKS N-> :");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }

    @Bean
    public AsyncItemProcessor<Cnab400, Cnab> asyncItemProcessor() {
        var asyncItemProcessor = new AsyncItemProcessor<Cnab400, Cnab>();
        asyncItemProcessor.setDelegate(processor());
        asyncItemProcessor.setTaskExecutor(taskExecutor());
        return asyncItemProcessor;
    }

    @Bean
    public AsyncItemWriter<Cnab> asyncItemWriter(ItemWriter<Cnab> writerCnab) {
        var asyncWritter = new AsyncItemWriter<Cnab>();
        asyncWritter.setDelegate(writerCnab);
        return asyncWritter;
    }

}
