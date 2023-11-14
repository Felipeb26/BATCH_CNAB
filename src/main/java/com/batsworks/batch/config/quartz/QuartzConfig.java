package com.batsworks.batch.config.quartz;

import com.batsworks.batch.utils.Utilities;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final JobLauncher jobLauncherAsync;
    private final JobLocator jobLocator;
    private final Environment environment;
    private static final String JOB_NAME = "CNAB_400_JOB";

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
       Utilities.temp();
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor registryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        registryBeanPostProcessor.setJobRegistry(jobRegistry);
        return registryBeanPostProcessor;
    }

    @Bean
    public JobDetail jobDetail() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", JOB_NAME);
        jobDataMap.put("jobLauncher", jobLauncherAsync);
        jobDataMap.put("jobLocator", jobLocator);

        return JobBuilder.newJob(QuartzJob.class)
                .withIdentity(JOB_NAME)
                .setJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger() {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(20)
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("CNAB_TRIGGER")
                .withSchedule(simpleScheduleBuilder)
                .build();
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setTriggers(trigger());
        scheduler.setQuartzProperties(quartzProperties());
        scheduler.setJobDetails(jobDetail());
        return scheduler;
    }

    @Bean
    public Properties quartzProperties() {
        Properties properties = new Properties();
        var jobStore = environment.getProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        var instanceName = environment.getProperty("org.quartz.scheduler.instanceName", "BATCH_CNAB");
        var threads = environment.getProperty("org.quartz.threadPool.threadCount", "1");

        properties.put("org.quartz.jobStore.class", jobStore);
        properties.put("org.quartz.scheduler.instanceName", instanceName);
        properties.put("org.quartz.threadPool.threadCount", threads);
        return properties;
    }

}
