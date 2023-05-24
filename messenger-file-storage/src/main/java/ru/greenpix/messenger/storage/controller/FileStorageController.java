package ru.greenpix.messenger.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.storage.service.FileStorageService;

import java.util.UUID;

@Tag(name = "Файловое хранилище")
@RestController
@RequestMapping("storage/file")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @GetMapping("{id}")
    @Operation(
            summary = "Получение файла по идентификатору"
    )
    public byte[] download(
            @PathVariable
            UUID id
    ) {
        return fileStorageService.downloadFile(id);
    }

}
