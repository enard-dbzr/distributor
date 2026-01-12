package com.plux.distribution.core.mediastorage.application.exception;

import com.plux.distribution.core.mediastorage.domain.MediaId;
import org.jetbrains.annotations.NotNull;

public class MediaNotFoundException extends Exception {

    public MediaNotFoundException(@NotNull MediaId mediaId) {
        super("Media with id " + mediaId.value() + " was not found.");
    }
}
