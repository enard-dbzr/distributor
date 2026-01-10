package com.plux.distribution.core.storage.application.dto;

import com.plux.distribution.core.storage.domain.FileMetadata;
import java.net.URL;
import org.jetbrains.annotations.NotNull;

public record StoredFileUrl(@NotNull URL url, @NotNull FileMetadata metadata) {}
