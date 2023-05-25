package ru.greenpix.messenger.storage.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.greenpix.messenger.storage.service.FileStorageService;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "Файловое хранилище (интеграционные запросы)")
@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FileStorageIntegrationController {

    private final FileStorageService fileStorageService;

    @PostMapping
    @Operation(
            summary = "Загрузка файла в хранилище"
    )
    public UUID upload(
            @RequestParam("file")
            MultipartFile file
    ) {
        try {
            return fileStorageService.uploadFile(file.getBytes(), file.getContentType());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Удаление файла по идентификатору"
    )
    public void delete(
            @PathVariable
            UUID id
    ) {
        fileStorageService.deleteFile(id);
    }

}
