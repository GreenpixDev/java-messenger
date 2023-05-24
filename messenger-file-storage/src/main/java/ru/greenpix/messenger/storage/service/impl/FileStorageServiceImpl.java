package ru.greenpix.messenger.storage.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import ru.greenpix.messenger.storage.exception.FileNotFoundException;
import ru.greenpix.messenger.storage.exception.InternalServerException;
import ru.greenpix.messenger.storage.service.FileStorageService;
import ru.greenpix.messenger.storage.settings.MinioSettings;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

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

        try (InputStream in = client.getObject(args)) {
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

    @Override
    public void deleteFile(UUID identifier) {
        Exception exception;

        try {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(settings.getBucket())
                    .object(identifier.toString())
                    .build()
            );
            return;
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

        logger.error("Cannot delete file " + identifier, exception);
        throw new InternalServerException();
    }
}
