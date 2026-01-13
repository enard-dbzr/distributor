package com.plux.distribution.core.mediastorage.application.port.in;

import com.plux.distribution.core.mediastorage.application.dto.StoredMedia;
import com.plux.distribution.core.mediastorage.application.exception.MediaNotFoundException;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import org.jetbrains.annotations.NotNull;

public interface DownloadMediaUseCase {

    @NotNull StoredMedia download(@NotNull MediaId id) throws MediaNotFoundException;
}
