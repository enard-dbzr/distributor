package com.plux.distribution.infrastructure.notifier.view.message.content;

import com.plux.distribution.infrastructure.notifier.view.message.attachment.MessageAttachmentView;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record SimpleMessageContentView(
        @NotNull String text,
        @NotNull List<MessageAttachmentView> attachments
) implements MessageContentView {

}
