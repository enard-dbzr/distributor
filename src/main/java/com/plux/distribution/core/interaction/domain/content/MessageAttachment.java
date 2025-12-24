package com.plux.distribution.core.interaction.domain.content;

import org.jetbrains.annotations.NotNull;

public sealed interface MessageAttachment permits MessageAttachment.ButtonAttachment {

    record ButtonAttachment(@NotNull String text, @NotNull String tag) implements MessageAttachment {}
}
