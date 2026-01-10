package com.plux.distribution.core.storage.application.port.in;

import com.plux.distribution.core.storage.domain.StorageKey;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;

public interface StoreFileUseCase {

    @NotNull StorageKey store(
            @NotNull String basePath,
            @NotNull InputStream data,
            @NotNull String contentType,
            long size
    );
}
