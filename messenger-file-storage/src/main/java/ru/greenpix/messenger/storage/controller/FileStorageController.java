package ru.greenpix.messenger.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.storage.service.FileStorageService;

import java.util.UUID;

@Tag(name = "Файловое хранилище")
@RestController
@RequestMapping("storage/files")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    // TODO хранить и отдавать content-type
    @GetMapping("{id}")
    @Operation(summary = "Получение файла по идентификатору")
    public ResponseEntity<Resource> download(
            @PathVariable
            UUID id
    ) {
        byte[] file = fileStorageService.downloadFile(id);
        return ResponseEntity.ok()
                .contentLength(file.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(file));
    }

}
