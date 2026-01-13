package com.plux.distribution.core.mediastorage.application.port.in;

import com.plux.distribution.core.mediastorage.application.dto.StoredMediaUrl;
import com.plux.distribution.core.mediastorage.application.exception.MediaNotFoundException;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import org.jetbrains.annotations.NotNull;

public interface GenerateMediaUrlUseCase {

    @NotNull StoredMediaUrl generateUrl(@NotNull MediaId id) throws MediaNotFoundException;
}
