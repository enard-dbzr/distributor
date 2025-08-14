package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.domain.message.attachment.AttachmentVisitor;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

class AttachmentRenderer implements AttachmentVisitor {

    @NotNull
    final RenderingContext context;

    public AttachmentRenderer(@NotNull RenderingContext context) {
        this.context = context;
    }

    @Override
    public void visit(ButtonAttachment attachment) {
        context.inlineKeyboardMarkupBuilder.keyboardRow(
                new InlineKeyboardRow(
                        InlineKeyboardButton
                                .builder()
                                .text(attachment.text())
                                .callbackData(attachment.tag())
                                .build()
                )
        );
        context.hasButtons = true;
    }
}
