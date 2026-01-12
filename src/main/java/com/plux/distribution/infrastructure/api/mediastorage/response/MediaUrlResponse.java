package com.plux.distribution.infrastructure.api.mediastorage.response;

import org.jetbrains.annotations.NotNull;

public record MediaUrlResponse(
        @NotNull String url,
        @NotNull String contentType
) {}
