package com.plux.distribution.core.mediastorage.application.exception;

import com.plux.distribution.core.mediastorage.domain.MediaId;
import org.jetbrains.annotations.NotNull;

public class DuplicateMediaIdException extends RuntimeException {

    public DuplicateMediaIdException(@NotNull MediaId mediaId) {
        super("Generated MediaId " + mediaId + " already exists.");
    }
}
