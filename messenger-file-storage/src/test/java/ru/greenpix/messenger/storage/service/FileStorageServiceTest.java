package ru.greenpix.messenger.storage.service;

import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.ErrorResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import ru.greenpix.messenger.storage.exception.FileNotFoundException;
import ru.greenpix.messenger.storage.service.impl.FileStorageServiceImpl;
import ru.greenpix.messenger.storage.settings.MinioSettings;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {

    /*
     * Тестовые данные
     */

    static UUID ID_TEST = UUID.fromString("4da6f9a6-4547-4769-b33c-06746f396d89");

    /*
     * Заглушки
     */

    @Mock
    MinioSettings settings;
    @Mock
    MinioClient client;
    @Mock
    Logger logger;

    /*
     * Тестируемый объект
     */

    @InjectMocks
    FileStorageServiceImpl fileStorageService;

    /*
     * Тесты
     */

    @DisplayName("Проверка загрузки файла в хранилище")
    @Test
    @SneakyThrows
    void uploadFileTest() {
        when(settings.getBucket()).thenReturn("test");
        when(client.putObject(any())).thenReturn(null);
        assertDoesNotThrow(() -> fileStorageService.uploadFile(new byte[0], "application/json"));
        verify(client, times(1))
                .putObject(any());
    }

    @DisplayName("Проверка успешного скачивания файла с хранилища")
    @Test
    @SneakyThrows
    void downloadFileTest() {
        GetObjectResponse response = mock(GetObjectResponse.class);
        when(response.readAllBytes()).thenReturn(new byte[0]);

        when(settings.getBucket()).thenReturn("test");
        when(client.getObject(any())).thenReturn(response);

        byte[] data = assertDoesNotThrow(() -> fileStorageService.downloadFile(ID_TEST));
        assertArrayEquals(new byte[0], data);
    }

    @DisplayName("Проверка скачивания несуществующего файла с хранилища")
    @Test
    @SneakyThrows
    void downloadNonExistsFileTest() {
        when(settings.getBucket()).thenReturn("test");
        when(client.getObject(any())).thenThrow(new ErrorResponseException(
                new ErrorResponse("NoSuchKey", "", "", "", "", "", ""),
                null,
                ""
        ));

        assertThrows(FileNotFoundException.class, () -> fileStorageService.downloadFile(ID_TEST));
    }
}
