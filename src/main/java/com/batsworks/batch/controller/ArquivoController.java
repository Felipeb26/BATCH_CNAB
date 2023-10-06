package com.batsworks.batch.controller;

import com.batsworks.batch.service.CnabService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/arquivo")
@RequiredArgsConstructor
public class ArquivoController {

    private final CnabService service;

    @PostMapping("upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, @RequestParam("type") String type) throws Exception {
        service.uploadCnabFile(file, type);
        return ResponseEntity.ok().body("Processamento Iniciado");
    }

}
