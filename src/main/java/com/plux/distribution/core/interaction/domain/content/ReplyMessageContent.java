package com.plux.distribution.core.interaction.domain.content;

import com.plux.distribution.core.interaction.domain.InteractionId;
import org.jetbrains.annotations.NotNull;

public record ReplyMessageContent(
        @NotNull InteractionContent original,
        @NotNull InteractionId replyTo
) implements InteractionContent {

}
