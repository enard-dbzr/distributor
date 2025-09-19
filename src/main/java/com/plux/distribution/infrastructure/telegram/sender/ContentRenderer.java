package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.infrastructure.telegram.port.message.GetTgMessageIdPort;
import com.plux.distribution.core.message.domain.content.MessageContentVisitor;
import com.plux.distribution.core.message.domain.content.ReplyMessageContent;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import org.jetbrains.annotations.NotNull;

class ContentRenderer implements MessageContentVisitor<Void> {

    private final @NotNull RenderingContext context;
    private final @NotNull GetTgMessageIdPort getTgMessageIdPort;

    public ContentRenderer(@NotNull RenderingContext context, @NotNull GetTgMessageIdPort getTgMessageIdPort) {
        this.context = context;
        this.getTgMessageIdPort = getTgMessageIdPort;
    }

    @Override
    public Void visit(@NotNull SimpleMessageContent content) {
        context.sendMessageBuilder.text(content.text());

        var attachmentRenderer = new AttachmentRenderer(context);
        content.attachments().forEach(attachment -> attachment.accept(attachmentRenderer));

        if (context.hasButtons) {
            context.sendMessageBuilder.replyMarkup(context.inlineKeyboardMarkupBuilder.build());
        }

        return null;
    }

    @Override
    public Void visit(ReplyMessageContent content) {
        var tgReplyMessageId = getTgMessageIdPort.getTgMessageId(content.replyTo());
        context.sendMessageBuilder.replyToMessageId(tgReplyMessageId.messageId());

        content.original().accept(this);

        return null;
    }
}
