package com.batsworks.batch.config;

import com.batsworks.batch.config.cnab.CnabSkipListenner;
import com.batsworks.batch.config.cnab.CnabSkipPolicy;
import com.batsworks.batch.config.utils.BatchParameters;
import com.batsworks.batch.config.utils.LoggingRequestInterceptor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class CustomBeansDefinition {

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    @Bean
    SkipPolicy skipPolicy() {
        return new CnabSkipPolicy();
    }

    @Bean
    CnabSkipListenner cnabSkipListenner(){
        return new CnabSkipListenner();
    }

    @Bean
    @StepScope
    BatchParameters parameters(@Value("#{jobParameters}") Map<String, Object> map) {
        return new BatchParameters(map);
    }
}
