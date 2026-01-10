package com.plux.distribution.core.storage.domain;

import org.jetbrains.annotations.NotNull;

public record FileMetadata(@NotNull String contentType, long size) {
}
