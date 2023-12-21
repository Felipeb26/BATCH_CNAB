package com.batsworks.batch.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BatsWorkClient", url = "https://batsworks-walls.onrender.com", configuration = {ServiceClientConfiguration.class})
public interface ServiceClient {

    @GetMapping("/v1/walls/{id}")
    ResponseEntity<Object> findWallById(@PathVariable Long id);

}
