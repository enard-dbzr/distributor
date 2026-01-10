package com.plux.distribution.core.storage.application.port.in;

import com.plux.distribution.core.storage.domain.StorageKey;
import com.plux.distribution.core.storage.application.dto.StoredFile;
import java.io.FileNotFoundException;
import org.jetbrains.annotations.NotNull;

public interface LoadFileUseCase {

    @NotNull StoredFile load(@NotNull StorageKey key) throws FileNotFoundException;
}
