package com.batsworks.batch.service.job;

import com.batsworks.batch.cnab.write.CnabWriteProcessor;
import com.batsworks.batch.domain.entity.CnabEntity;
import com.batsworks.batch.repository.CnabRepository;
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
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.batsworks.batch.utils.Files.randomFileName;
import static com.batsworks.batch.utils.Formats.actualDateString;

@Configuration
@RequiredArgsConstructor
public class GenerateCNABService {

    private final PlatformTransactionManager transactionManager;
    private final CnabRepository cnabRepository;
    private final JobRepository jobRepository;

    @Bean
    Job jobWriteCnab(Step stepWrite, JobRepository repository) {
        return new JobBuilder("WRITE_CNAB_400_JOB_".concat(actualDateString()), repository)
                .flow(stepWrite).end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step stepWrite(RepositoryItemReader<CnabEntity> repositoryItemReader, FlatFileItemWriter<CnabEntity> fileItemWriter) {
        return new StepBuilder("CNAB_TO_FILE", jobRepository)
                .<CnabEntity, CnabEntity>chunk(350, transactionManager)
                .reader(repositoryItemReader)
//                .processor(cnabWriteProcessor())
                .writer(fileItemWriter)
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

    @Bean
    @StepScope
    public RepositoryItemReader<CnabEntity> repositoryItemReader(@Value("#{jobParameters[id]}") Long id) {
        Map<String, Sort.Direction> map = new HashMap<>();
        map.put("linha", Sort.Direction.ASC);

        return new RepositoryItemReaderBuilder<CnabEntity>()
                .name("READER_CNAB " + actualDateString())
                .repository(cnabRepository)
                .arguments(List.of(id))
                .methodName("findAllById")
                .sorts(map)
                .build();
    }

    @Bean
    public CnabWriteProcessor cnabWriteProcessor(){
     return new CnabWriteProcessor();
    }

    @Bean
    public FlatFileItemWriter<CnabEntity> fileItemWriter() {
        FlatFileItemWriter<CnabEntity> writer = new FlatFileItemWriter<>();
        writer.setName("WRITE_CNAB");

        var file = System.getProperty("user.dir").concat("/" + randomFileName());

        writer.setResource(new FileSystemResource(file));

        DelimitedLineAggregator<CnabEntity> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("");

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



}
