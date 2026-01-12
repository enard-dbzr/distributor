package com.plux.distribution.core.mediastorage.application.port.in;

import com.plux.distribution.core.mediastorage.application.exception.MediaNotFoundException;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import com.plux.distribution.core.storage.application.dto.StoredFileUrl;
import org.jetbrains.annotations.NotNull;

public interface GenerateMediaUrlUseCase {

    @NotNull StoredFileUrl generateUrl(@NotNull MediaId id) throws MediaNotFoundException;
}
