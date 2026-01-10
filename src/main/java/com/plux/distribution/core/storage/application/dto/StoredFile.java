package com.plux.distribution.core.storage.application.dto;

import com.plux.distribution.core.storage.domain.FileMetadata;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;

public record StoredFile(
        @NotNull InputStream data,
        @NotNull FileMetadata metadata
) {}
