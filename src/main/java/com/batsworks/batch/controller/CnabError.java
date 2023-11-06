package com.batsworks.batch.controller;

import com.batsworks.batch.service.CnabErorsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/cnab-error", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Tag(name = "Endpoint Error")
public class CnabError {

    private final CnabErorsService service;

    @GetMapping("/")
    public ResponseEntity<Object> string() {
        return ResponseEntity.ok(service.string(PageRequest.of(0, 50)));
    }

}
