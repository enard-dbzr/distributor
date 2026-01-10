package com.plux.distribution.core.storage.application.port.in;

import com.plux.distribution.core.storage.domain.StorageKey;
import org.jetbrains.annotations.NotNull;

public interface DeleteFileUseCase {

    void delete(@NotNull StorageKey key);
}
