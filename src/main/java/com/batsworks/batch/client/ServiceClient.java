package com.batsworks.batch.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "BatsWorkClient", url = "http://localhost:9092/batsworks/", configuration = {ServiceClientConfiguration.class})
public interface ServiceClient {

    @GetMapping("/v1/medicos")
    ResponseEntity<Object> findWallById();

}
