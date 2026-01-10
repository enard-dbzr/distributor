package com.plux.distribution.core.storage.application.port.out;

import com.plux.distribution.core.storage.domain.FileMetadata;
import com.plux.distribution.core.storage.application.exception.ObjectAlreadyExistsException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import org.jetbrains.annotations.NotNull;

public interface StoragePort {
    void save(
            @NotNull String key,
            @NotNull InputStream data,
            @NotNull String contentType,
            long size
    ) throws ObjectAlreadyExistsException;

    @NotNull InputStream load(@NotNull String key) throws FileNotFoundException;

    @NotNull FileMetadata loadMetadata(@NotNull String key) throws FileNotFoundException;

    void delete(@NotNull String key);

    @NotNull URL generateUrl(String key, Duration ttl);
}
