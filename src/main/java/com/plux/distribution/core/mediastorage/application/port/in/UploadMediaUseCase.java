package com.plux.distribution.core.mediastorage.application.port.in;

import com.plux.distribution.core.mediastorage.domain.MediaId;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UploadMediaUseCase {

    @NotNull MediaId upload(
            @NotNull InputStream data,
            @NotNull String contentType,
            @NotNull String filename, long size,
            @Nullable String scope
    );
}
