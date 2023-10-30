package com.batsworks.batch.service.job;

import com.batsworks.batch.domain.entity.CnabEntity;
import com.batsworks.batch.repository.CnabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

import static com.batsworks.batch.config.utils.Utilities.actualDateString;

@Configuration
@RequiredArgsConstructor
public class GenerateCNABService {

    private final PlatformTransactionManager transactionManager;
    private final CnabRepository cnabRepository;
    private final JobRepository jobRepository;

    @Bean
    public RepositoryItemReader<CnabEntity> repositoryItemReader() {
        Map<String, Sort.Direction> map = new HashMap<>();
        map.put("id", Sort.Direction.ASC);
        return new RepositoryItemReaderBuilder<CnabEntity>()
                .name("READER_CNAB"+ actualDateString())
                .repository(cnabRepository)
                .methodName("findAll")
                .sorts(map)
                .build();
    }

    @Bean
    public ItemProcessor<CnabEntity, CnabEntity> itemProcessor() {
        return item -> item;
    }

    @Bean
    public FlatFileItemWriter<CnabEntity> fileItemWriter() {
        FlatFileItemWriter<CnabEntity> writer = new FlatFileItemWriter<>();
        writer.setName("WRITE_CNAB");

        writer.setResource(new FileSystemResource("/home/felipes/IdeaProjects/BATCH_CNAB/teste.rem"));

        DelimitedLineAggregator<CnabEntity> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(" ");

        BeanWrapperFieldExtractor<CnabEntity> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"identRegistro", "agenciaDebito", "digitoAgencia",
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
                "cep", "sufixoCEP", "segundaMensagem", "sequencialRegistro"});

        lineAggregator.setFieldExtractor(fieldExtractor);
        writer.setLineAggregator(lineAggregator);

        return writer;
    }

    @Bean
    public Step stepWrite(RepositoryItemReader<CnabEntity> repositoryItemReader, ItemProcessor<CnabEntity, CnabEntity> itemProcessor, FlatFileItemWriter<CnabEntity> fileItemWriter) {
        return new StepBuilder("CNAB_TO_FILE", jobRepository)
                .<CnabEntity, CnabEntity>chunk(100, transactionManager)
                .reader(repositoryItemReader)
                .processor(itemProcessor)
                .writer(fileItemWriter)
                .build();
    }

    @Bean
    Job jobWriteCnab(Step stepWrite, JobRepository repository) {
        return new JobBuilder("WRITE_CNAB_400_JOB_" + System.currentTimeMillis(), repository)
                .start(stepWrite)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    JobLauncher asyncWrite(JobRepository jobRepository, TaskExecutor taskExecutor) throws Exception {
        var jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

}
