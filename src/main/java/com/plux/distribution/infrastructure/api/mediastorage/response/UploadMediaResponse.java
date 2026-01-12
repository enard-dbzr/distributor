package com.plux.distribution.infrastructure.api.mediastorage.response;

import com.plux.distribution.core.mediastorage.domain.MediaId;
import org.jetbrains.annotations.NotNull;

public record UploadMediaResponse(
        @NotNull String mediaId
) {

    public static UploadMediaResponse of(@NotNull MediaId mediaId) {
        return new UploadMediaResponse(mediaId.value().toString());
    }
}