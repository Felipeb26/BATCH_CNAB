package com.batsworks.batch.controller;

import com.batsworks.batch.domain.enums.CnabType;
import com.batsworks.batch.domain.records.DefaultMessage;
import com.batsworks.batch.service.CnabService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/arquivo")
@RequiredArgsConstructor
public class ArquivoController {

    private final CnabService service;

    @PostMapping("upload")
    @CacheEvict("CacheManager")
    public ResponseEntity<DefaultMessage> upload(@RequestParam("file") MultipartFile file, @RequestParam("type") CnabType type) {
        return ResponseEntity.ok().body(service.uploadCnabFile(file, type));
    }

    @GetMapping("download")
    public ResponseEntity<DefaultMessage> download() {
        return ResponseEntity.ok(service.downloadCnab());
    }

    @GetMapping("/")
    public String string(){
        return service.string();
    }
}
