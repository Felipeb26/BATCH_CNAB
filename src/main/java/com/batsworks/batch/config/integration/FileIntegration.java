//package com.batsworks.batch.config.integration;
//
//import com.batsworks.batch.config.utils.BatchParameters;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.integration.launch.JobLaunchingGateway;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.integration.annotation.IntegrationComponentScan;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.channel.PublishSubscribeChannel;
//import org.springframework.integration.config.EnableIntegration;
//import org.springframework.integration.dsl.IntegrationFlow;
//import org.springframework.integration.dsl.Pollers;
//import org.springframework.integration.file.DefaultFileNameGenerator;
//import org.springframework.integration.file.FileReadingMessageSource;
//import org.springframework.integration.file.FileWritingMessageHandler;
//import org.springframework.integration.file.filters.SimplePatternFileListFilter;
//import org.springframework.integration.file.support.FileExistsMode;
//import org.springframework.integration.handler.LoggingHandler;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.time.Duration;
//
//@Component
//@EnableIntegration
//@IntegrationComponentScan
//@RequiredArgsConstructor
//public class FileIntegration {
//
//    @Value("${configuration.default_folder:tmp}")
//    protected String path;
//    private final JobLauncher jobLauncherAsync;
//    private final Job jobCnab;
//    private final BatchParameters parameters;
//
//    @Bean
//    public IntegrationFlow integrationFlow(FileReadingMessageSource fileReadingMessageSource) {
//        return IntegrationFlow.from(fileReadingMessageSource,
//                        source -> source.poller(Pollers.fixedDelay(Duration.ofSeconds(10)).maxMessagesPerPoll(1)))
//                .channel(directChannel())
//                .handle(fileMessageHandler())
//                .transform(fileMessageJobRequest())
//                .handle(jobLaunchingGateway())
//                .channel(publishSubscribeChannel())
//                .log(LoggingHandler.Level.DEBUG)
//                .get();
//    }
//
//    @Bean
//    public FileReadingMessageSource fileReadingMessageSource() {
//        var messageSource = new FileReadingMessageSource();
//        messageSource.setDirectory(new File(path));
//        messageSource.setAutoCreateDirectory(true);
//        messageSource.setFilter(new SimplePatternFileListFilter("*.rem"));
//        return messageSource;
//    }
//
//    @Bean
//    public MessageChannel directChannel() {
//        return new DirectChannel();
//    }
//
//    @Bean
//    public PublishSubscribeChannel publishSubscribeChannel() {
//        return new PublishSubscribeChannel();
//    }
//
//    public MessageHandler fileMessageHandler() {
//        var messageHandler = new FileWritingMessageHandler(new File(path));
//        messageHandler.setFileExistsMode(FileExistsMode.REPLACE);
//        messageHandler.setDeleteSourceFiles(Boolean.TRUE);
//        messageHandler.setFileNameGenerator(new DefaultFileNameGenerator());
//        messageHandler.setFileNameGenerator(fileNameGenerator());
//        messageHandler.setRequiresReply(Boolean.FALSE);
//        return messageHandler;
//    }
//
//    public DefaultFileNameGenerator fileNameGenerator() {
//        var fileNameGenerator = new DefaultFileNameGenerator();
//        fileNameGenerator.setExpression("payload.name +'.processing'");
//        return fileNameGenerator;
//    }
//
//    public FileMessageJobRequest fileMessageJobRequest() {
//        var transformer = new FileMessageJobRequest(parameters);
//        transformer.setJob(jobCnab);
//        return transformer;
//    }
//
//    public JobLaunchingGateway jobLaunchingGateway() {
//        return new JobLaunchingGateway(jobLauncherAsync);
//    }
//}
