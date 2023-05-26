package ru.greenpix.messenger.storage.service.impl;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import ru.greenpix.messenger.storage.exception.FileNotFoundException;
import ru.greenpix.messenger.storage.exception.InternalServerException;
import ru.greenpix.messenger.storage.service.FileStorageService;
import ru.greenpix.messenger.storage.settings.MinioSettings;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    /**
     * Код ошибки, который возвращает MinIO, когда файл не найден по идентификатору
     */
    private static final String NO_SUCH_KEY_CODE = "NoSuchKey";

    private final MinioSettings settings;
    private final MinioClient client;
    private final Logger logger;

    @Override
    public UUID uploadFile(byte[] content) {
        UUID id = UUID.randomUUID();
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(settings.getBucket())
                    .object(id.toString())
                    .stream(new ByteArrayInputStream(content), content.length, -1)
                    .build()
            );
            logger.info("Uploaded new file: {}", id);
        }
        catch (Exception e) {
            logger.error("Cannot upload file", e);
            throw new InternalServerException();
        }
        return id;
    }

    @Override
    public byte[] downloadFile(UUID identifier) {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(settings.getBucket())
                .object(identifier.toString())
                .build();
        Exception exception;

        try (GetObjectResponse in = client.getObject(args)) {
            logger.trace("Downloading file {}", identifier);
            return in.readAllBytes();
        }
        catch (ErrorResponseException e) {
            if (NO_SUCH_KEY_CODE.equals(e.errorResponse().code())) {
                throw new FileNotFoundException();
            }
            else {
                exception = e;
            }
        }
        catch (Exception e) {
            exception = e;
        }

        logger.error("Cannot download file " + identifier, exception);
        throw new InternalServerException();
    }

}
