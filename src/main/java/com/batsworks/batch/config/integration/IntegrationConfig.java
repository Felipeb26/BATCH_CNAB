package com.batsworks.batch.config.integration;

import com.batsworks.batch.config.utils.BatchParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;

@Component
@EnableIntegration
@IntegrationComponentScan
@RequiredArgsConstructor
public class IntegrationConfig {

    private final JobRepository repository;
    private final BatchParameters parameters;
    private final TaskExecutor taskExecutor;

    public IntegrationFlow integrationFlow() {
        return IntegrationFlows.from(fileReadingMessageSource(),
                        source -> source.poller(Pollers.fixedDelay(Duration.ofSeconds(5)).maxMessagesPerPoll(1)))
                .channel(directChannel())
                .log().get();
    }

    public FileReadingMessageSource fileReadingMessageSource() {
        var path = parameters.getParameters().getOrDefault("file", "");
        var messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(String.valueOf(path)));
        messageSource.setFilter(new SimplePatternFileListFilter("*.rem"));
        return messageSource;
    }

    public DirectChannel directChannel() {
        return new DirectChannel();
    }

    public JobLaunchingGateway jobLaunchingGateway() {
        var simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(repository);
        simpleJobLauncher.setTaskExecutor(taskExecutor);
        return new JobLaunchingGateway(simpleJobLauncher);
    }

}
