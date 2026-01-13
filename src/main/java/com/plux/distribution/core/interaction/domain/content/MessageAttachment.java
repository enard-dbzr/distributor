package com.plux.distribution.core.interaction.domain.content;

import com.plux.distribution.core.mediastorage.domain.MediaId;
import org.jetbrains.annotations.NotNull;

public sealed interface MessageAttachment {

    record ButtonAttachment(@NotNull String text, @NotNull String tag) implements MessageAttachment {}

    record MediaAttachment(@NotNull MediaId mediaId, @NotNull DisplayType displayType) implements MessageAttachment {

        public enum DisplayType {PHOTO, DOCUMENT}
    }
}
