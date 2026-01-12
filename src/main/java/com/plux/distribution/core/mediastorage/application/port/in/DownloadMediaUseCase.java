package com.plux.distribution.core.mediastorage.application.port.in;

import com.plux.distribution.core.mediastorage.application.exception.MediaNotFoundException;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import com.plux.distribution.core.storage.application.dto.StoredFile;
import org.jetbrains.annotations.NotNull;

public interface DownloadMediaUseCase {

    @NotNull StoredFile download(@NotNull MediaId id) throws MediaNotFoundException;
}
