package com.plux.distribution.core.mediastorage.application.dto;

import com.plux.distribution.core.storage.application.dto.StoredFile;
import org.jetbrains.annotations.NotNull;

public record StoredMedia(@NotNull StoredFile file, @NotNull MediaMetadata metadata) {
}
