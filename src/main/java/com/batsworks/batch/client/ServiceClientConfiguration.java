package com.batsworks.batch.client;

import com.batsworks.batch.config.exception.BussinesException;
import com.batsworks.batch.config.exception.StatusEnum;
import feign.Response;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.codec.StringDecoder;
import feign.form.spring.SpringFormEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
public class ServiceClientConfiguration {

    private final Environment env;
    private final ObjectFactory<HttpMessageConverters> converters;

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorDecoder.class)
    public FeignClientExceptionErrorDecoder commonFeignErrorDecoder() {
        return new FeignClientExceptionErrorDecoder();
    }

    public static class FeignClientExceptionErrorDecoder implements ErrorDecoder {
        private final StringDecoder stringDecoder = new StringDecoder();

        @Override
        public BussinesException decode(final String methodKey, Response response) {
            String message = null;
            try {
                if (response != null) {
                    message = stringDecoder.decode(response, String.class).toString();
                    if (response.status() > 400)
                        message = response.reason();

                    String url = response.request().url();
                    log.info("URL: {} \tMESSGAE: {}", url, message);
                    if (nonNull(message) && !message.isBlank())
                        return new BussinesException(HttpStatusCode.valueOf(response.status()), message);
                    return new BussinesException(HttpStatusCode.valueOf(response.status()), nonNull(response.headers()) ? response.headers().toString() : StatusEnum.UNKNOW_ERROR.getError());
                }
            } catch (IOException ioex) {
                log.error(ioex.getMessage(), ioex);
                return new BussinesException(HttpStatusCode.valueOf(response.status()), "UNKNOW_ERROR");
            }
            return new BussinesException(HttpStatus.SERVICE_UNAVAILABLE, "UNKNOW_ERROR");
        }
    }
}