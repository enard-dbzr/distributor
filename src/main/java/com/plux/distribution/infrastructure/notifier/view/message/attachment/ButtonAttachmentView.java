package com.plux.distribution.infrastructure.notifier.view.message.attachment;

import org.jetbrains.annotations.NotNull;

public record ButtonAttachmentView(@NotNull String text, @NotNull String tag) implements MessageAttachmentView {

}
