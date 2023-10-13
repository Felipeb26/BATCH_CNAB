package com.batsworks.batch.controller;

import com.batsworks.batch.domain.enums.CnabType;
import com.batsworks.batch.service.CnabService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/arquivo")
@RequiredArgsConstructor
public class ArquivoController {

    private final CnabService service;

    @PostMapping("upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, @RequestParam("type") CnabType type) throws Exception {
        service.uploadCnabFile(file, type);
        return ResponseEntity.ok().body("Processamento Iniciado");
    }

    @GetMapping("download")
    public ResponseEntity<Object> download(){
        return ResponseEntity.ok(service.downloadCnab());
    }

}
