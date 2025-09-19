package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.infrastructure.telegram.port.chat.GetTgChatIdPort;
import com.plux.distribution.core.message.application.port.out.MessageSenderPort;
import com.plux.distribution.infrastructure.telegram.port.message.GetTgMessageIdPort;
import com.plux.distribution.infrastructure.telegram.port.message.TgMessageGlobalId;
import com.plux.distribution.infrastructure.telegram.port.message.TgMessageLinker;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.participant.Participant;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramMessageSender implements MessageSenderPort {

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
    public void send(@NotNull MessageId messageId, @NotNull Participant recipient,
            @NotNull MessageContent messageContent) {
        var context = new RenderingContext();

        var uidExtractor = new ChatIdExtractor();
        recipient.accept(uidExtractor);

        var tgId = getTgChatIdPort.getTgChatId(uidExtractor.getChatId());

        context.sendMessageBuilder.chatId(tgId);

        var renderer = new ContentRenderer(context, getTgMessageIdPort);

        messageContent.accept(renderer);

        var tgMessage = context.sendMessageBuilder.build();

        try {
            var res = client.execute(tgMessage);

            tgMessageLinker.link(messageId, new TgMessageGlobalId(res.getMessageId(), tgId));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}