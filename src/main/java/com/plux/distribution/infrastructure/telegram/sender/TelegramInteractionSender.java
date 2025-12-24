package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.application.port.out.InteractionSenderPort;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.infrastructure.telegram.port.TgChatLinker;
import com.plux.distribution.infrastructure.telegram.port.TgMessageGlobalId;
import com.plux.distribution.infrastructure.telegram.port.TgMessageLinker;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramInteractionSender implements InteractionSenderPort {

    private final @NotNull TelegramClient client;
    private final @NotNull TgChatLinker tgChatLinker;
    private final @NotNull TgMessageLinker tgMessageLinker;


    public TelegramInteractionSender(@NotNull TelegramClient client,
            @NotNull TgChatLinker tgChatLinker, @NotNull TgMessageLinker tgMessageLinker) {
        this.client = client;
        this.tgChatLinker = tgChatLinker;
        this.tgMessageLinker = tgMessageLinker;
    }

    @Override
    public void send(@NotNull InteractionId interactionId, @NotNull Participant recipient,
            @NotNull InteractionContent interactionContent) {
        var context = new RenderingContext();
        context.sendMessageBuilder.parseMode("Markdown");

        if (!(recipient instanceof ChatParticipant(ChatId chatId))) {
            throw new IllegalArgumentException("Not chat participants is not allowed here");
        }

        var tgId = tgChatLinker.getTgChatId(chatId);

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
                var tgReplyMessageId = tgMessageLinker.getTgMessageId(c.replyTo());
                context.sendMessageBuilder.replyToMessageId(tgReplyMessageId.messageId());

                constructContent(context, c.original());
            }
            case ButtonClickContent _ -> throw new UnsupportedOperationException();
        }
    }
}