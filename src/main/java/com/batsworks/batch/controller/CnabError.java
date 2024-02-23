package com.batsworks.batch.controller;

import com.batsworks.batch.service.CnabErrorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    private final CnabErrorService service;

    @GetMapping("/")
    public ResponseEntity<Object> string(@PageableDefault(direction = Sort.Direction.DESC, sort = "dataCadastro") Pageable pageable) {
        return ResponseEntity.ok(service.errosPerPage(pageable));
    }

}
