package com.plux.distribution.infrastructure.telegram.sender;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

class RenderingContext {

    @NotNull
    final SendMessage.SendMessageBuilder<?, ?> sendMessageBuilder;
    @NotNull
    final InlineKeyboardMarkup.InlineKeyboardMarkupBuilder<?, ?> inlineKeyboardMarkupBuilder;
    boolean hasButtons = false;

    RenderingContext() {
        sendMessageBuilder = SendMessage.builder();
        inlineKeyboardMarkupBuilder = InlineKeyboardMarkup.builder();
    }
}
