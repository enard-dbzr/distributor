package com.plux.distribution.core.mediastorage.domain;

import com.plux.distribution.core.storage.domain.StorageKey;
import org.jetbrains.annotations.NotNull;

public record Media(@NotNull MediaId id, @NotNull StorageKey storageKey) {}
