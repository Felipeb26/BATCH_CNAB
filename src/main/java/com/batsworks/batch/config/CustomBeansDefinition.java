package com.batsworks.batch.config;

import com.batsworks.batch.cnab.read.CnabJobListener;
import com.batsworks.batch.cnab.read.CnabProcessor;
import com.batsworks.batch.cnab.read.CnabSkipListenner;
import com.batsworks.batch.cnab.read.CnabSkipPolicy;
import com.batsworks.batch.domain.entity.BatchParameters;
import com.batsworks.batch.repository.ArquivoRepository;
import com.batsworks.batch.service.CnabErrorService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class CustomBeansDefinition {

    @Bean
    SkipPolicy skipPolicy(CnabErrorService cnabErrorService, ArquivoRepository arquivoRepository, BatchParameters batchParameters) {
        return new CnabSkipPolicy(cnabErrorService, arquivoRepository, batchParameters);
    }

    @Bean
    CnabSkipListenner cnabSkipListenner() {
        return new CnabSkipListenner();
    }

    @Bean
    CnabJobListener cnabJobListener() {
        return new CnabJobListener();
    }

    @Bean
    CnabProcessor processor() {
        return new CnabProcessor();
    }

    @Bean
    @StepScope
    public BatchParameters batchParameters(@Value("#{jobParameters}") Map<String, Object> map) {
        return new BatchParameters(map);
    }

    @Bean
    BatchParameters parameters() {
        return new BatchParameters();
    }

    @Bean
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(99);
        taskExecutor.setCorePoolSize(99);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("BATSWORKS N-> :");
        taskExecutor.setKeepAliveSeconds(10);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return taskExecutor;
    }
}
