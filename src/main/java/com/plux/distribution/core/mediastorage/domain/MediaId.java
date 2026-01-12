package com.plux.distribution.core.mediastorage.domain;

import java.util.UUID;

public record MediaId(UUID value) {

    public static MediaId generate() {
        return new MediaId(UUID.randomUUID());
    }
}
