package com.plux.distribution.infrastructure.notifier.view.message.content;

import org.jetbrains.annotations.NotNull;

public record ReplyContentView(
        @NotNull MessageContentView original,
        @NotNull Long replyToMessageId
) implements MessageContentView {

}
