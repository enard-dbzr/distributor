package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.MediaAttachment;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

class RenderingContext {

    final long tgChatId;

    @Nullable String text;
    @Nullable Integer replyToMessageId;

    final List<ButtonAttachment> buttons = new ArrayList<>();
    final List<MediaAttachment> mediaAttachments = new ArrayList<>();

    RenderingContext(long tgChatId) {
        this.tgChatId = tgChatId;
    }
}
