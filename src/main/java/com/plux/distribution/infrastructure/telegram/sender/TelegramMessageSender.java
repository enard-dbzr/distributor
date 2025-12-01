package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.application.port.out.MessageSenderPort;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.infrastructure.telegram.port.chat.GetTgChatIdPort;
import com.plux.distribution.infrastructure.telegram.port.message.GetTgMessageIdPort;
import com.plux.distribution.infrastructure.telegram.port.message.TgMessageGlobalId;
import com.plux.distribution.infrastructure.telegram.port.message.TgMessageLinker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramMessageSender implements MessageSenderPort {

    private static final Logger log = LogManager.getLogger(TelegramMessageSender.class);
    private final @NotNull TelegramClient client;
    private final @NotNull GetTgChatIdPort getTgChatIdPort;
    private final @NotNull TgMessageLinker tgMessageLinker;
    private final @NotNull GetTgMessageIdPort getTgMessageIdPort;


    public TelegramMessageSender(@NotNull TelegramClient client,
            @NotNull GetTgChatIdPort getTgChatIdPort, @NotNull TgMessageLinker tgMessageLinker,
            @NotNull GetTgMessageIdPort getTgMessageIdPort) {
        this.client = client;
        this.getTgChatIdPort = getTgChatIdPort;
        this.tgMessageLinker = tgMessageLinker;
        this.getTgMessageIdPort = getTgMessageIdPort;
    }

    @Override
    public void send(@NotNull InteractionId interactionId, @NotNull Participant recipient,
            @NotNull InteractionContent interactionContent) {
        var context = new RenderingContext();
        context.sendMessageBuilder.parseMode("Markdown");

        if (!(recipient instanceof ChatParticipant(ChatId chatId))) {
            throw new IllegalArgumentException("Not chat participants is not allowed here");
        }

        var tgId = getTgChatIdPort.getTgChatId(chatId);

        context.sendMessageBuilder.chatId(tgId);

        constructContent(context, interactionContent);

        var tgMessage = context.sendMessageBuilder.build();

        try {
            var res = client.execute(tgMessage);

            tgMessageLinker.link(interactionId, new TgMessageGlobalId(res.getMessageId(), tgId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void constructContent(@NotNull RenderingContext context, InteractionContent content) {
        switch (content) {
            case SimpleMessageContent c -> {
                context.sendMessageBuilder.text(c.text());

                for (var attachment : c.attachments()) {
                    switch (attachment) {
                        case ButtonAttachment a -> {
                            context.inlineKeyboardMarkupBuilder.keyboardRow(
                                    new InlineKeyboardRow(
                                            InlineKeyboardButton
                                                    .builder()
                                                    .text(a.text())
                                                    .callbackData(a.tag())
                                                    .build()
                                    )
                            );
                            context.hasButtons = true;
                        }
                    }

                    if (context.hasButtons) {
                        context.sendMessageBuilder.replyMarkup(context.inlineKeyboardMarkupBuilder.build());
                    }
                }
            }
            case ReplyMessageContent c -> {
                var tgReplyMessageId = getTgMessageIdPort.getTgMessageId(c.replyTo());
                context.sendMessageBuilder.replyToMessageId(tgReplyMessageId.messageId());

                constructContent(context, c);
            }
        }
    }
}