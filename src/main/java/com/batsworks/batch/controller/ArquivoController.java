package com.batsworks.batch.controller;

import com.batsworks.batch.domain.enums.FileType;
import com.batsworks.batch.domain.records.ArquivoDTO;
import com.batsworks.batch.domain.records.DefaultMessage;
import com.batsworks.batch.domain.records.PageDTO;
import com.batsworks.batch.service.ArquivoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.batsworks.batch.utils.Files.randomFileName;

@RestController
@RequestMapping("/v1/arquivo")
@RequiredArgsConstructor
@Tag(name = "Endpoint Arquivo")
public class ArquivoController {

    private final ArquivoService service;

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<DefaultMessage> upload(@RequestParam("file") MultipartFile file,
                                                 @RequestParam(value = "type", defaultValue = "CNAB400") FileType type,
                                                 @RequestParam(value = "observacao", required = false) String observacao) {
        return ResponseEntity.ok().body(service.uploadCnabFile(file, type, observacao));
    }

    @GetMapping("download")
    public ResponseEntity<Object> download(@RequestParam(value = "retorno", defaultValue = "false") Boolean retorno,
                                           @RequestParam(value = "arquivo") Long idArquivo) {
        var response = service.downloadCnab(retorno, idArquivo);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=%s".formatted(randomFileName()))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(response.length)
                .body(response);
    }

    @GetMapping
    public PageDTO findAllByPage(@RequestParam int pageNumber, @RequestParam int size) {
        return service.findAllByPage(PageRequest.of(pageNumber, size));
    }

    @GetMapping("/{id}")
    public ArquivoDTO arquivoByID(@PathVariable Long id) {
        return service.findArquivoByID(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteArquivo(@PathVariable Long id) {
        service.deleteArquivo(id);
    }
}
