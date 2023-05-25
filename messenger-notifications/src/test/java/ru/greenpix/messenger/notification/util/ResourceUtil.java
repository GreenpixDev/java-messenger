package ru.greenpix.messenger.notification.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ResourceUtil {

    public static byte[] getResource(String name) {
        try {
            return Files.readAllBytes(Path.of(Objects.requireNonNull(ResourceUtil.class.getResource(name)).toURI()));
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
