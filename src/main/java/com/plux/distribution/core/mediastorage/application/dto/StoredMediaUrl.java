package com.plux.distribution.core.mediastorage.application.dto;

import com.plux.distribution.core.storage.application.dto.StoredFileUrl;
import org.jetbrains.annotations.NotNull;

public record StoredMediaUrl(@NotNull StoredFileUrl fileUrl, @NotNull MediaMetadata metadata) {
}
