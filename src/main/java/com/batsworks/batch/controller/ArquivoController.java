package com.batsworks.batch.controller;

import com.batsworks.batch.config.utils.Utilities;
import com.batsworks.batch.domain.enums.CnabType;
import com.batsworks.batch.domain.records.DefaultMessage;
import com.batsworks.batch.service.CnabService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/arquivo")
@RequiredArgsConstructor
@Tag(name = "Endpoint Arquivo")
public class ArquivoController {

    private final CnabService service;

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<DefaultMessage> upload(@RequestParam("file") MultipartFile file, @RequestParam("type") CnabType type) {
        return ResponseEntity.ok().body(service.uploadCnabFile(file, type));
    }

    @GetMapping("download")
    public ResponseEntity<Object> download(@RequestParam(value = "retorno", defaultValue = "true") Boolean retorno,
                                           @RequestParam(value = "arquivo") Long idArquivo) {
        var response = service.downloadCnab(retorno, idArquivo);


        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=%s".formatted(Utilities.randomFileName()))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(response.length)
                .body(response);
    }

    @GetMapping("/")
    public String string() {
        return service.string();
    }

    @GetMapping("/reset")
    public Object object() {
        return service.resetTempFile();
    }
}
