package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.domain.message.content.MessageContentVisitor;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import org.jetbrains.annotations.NotNull;

class ContentRenderer implements MessageContentVisitor {

    @NotNull
    private final RenderingContext context;

    public ContentRenderer(@NotNull RenderingContext context) {
        this.context = context;
    }

    @Override
    public void visit(@NotNull SimpleMessageContent content) {
        context.sendMessageBuilder.text(content.text());

        var attachmentRenderer = new AttachmentRenderer(context);
        content.attachments().forEach(attachment -> attachment.accept(attachmentRenderer));

        if (context.hasButtons) {
            context.sendMessageBuilder.replyMarkup(context.inlineKeyboardMarkupBuilder.build());
        }
    }
}
