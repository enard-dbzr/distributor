package com.plux.distribution.core.interaction.domain.content;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public record SimpleMessageContent(
        @NotNull String text, @NotNull List<MessageAttachment> attachments
) implements InteractionContent {

}
