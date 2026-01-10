package com.plux.distribution.core.storage.application.port.in;

import com.plux.distribution.core.storage.application.dto.StoredFileUrl;
import com.plux.distribution.core.storage.domain.StorageKey;
import java.io.FileNotFoundException;
import org.jetbrains.annotations.NotNull;

public interface GenerateFileUrlUseCase {

    @NotNull StoredFileUrl generateUrl(@NotNull StorageKey key) throws FileNotFoundException;
}
