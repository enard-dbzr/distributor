package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.application.port.out.specific.telegram.GetTgUserIdPort;
import com.plux.distribution.application.port.out.MessageSenderPort;
import com.plux.distribution.application.port.out.specific.telegram.TgMessageLinker;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.participant.Participant;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramMessageSender implements MessageSenderPort {

    private final @NotNull TelegramClient client;
    private final @NotNull GetTgUserIdPort getTgUserIdPort;
    private final @NotNull TgMessageLinker tgMessageLinker;


    public TelegramMessageSender(@NotNull TelegramClient client,
            @NotNull GetTgUserIdPort getTgUserIdPort, @NotNull TgMessageLinker tgMessageLinker) {
        this.client = client;
        this.getTgUserIdPort = getTgUserIdPort;
        this.tgMessageLinker = tgMessageLinker;
    }

    @Override
    public void send(@NotNull MessageId messageId, @NotNull Participant recipient,
            @NotNull MessageContent messageContent) {
        var context = new RenderingContext();

        var uidExtractor = new UserIdExtractor();
        recipient.accept(uidExtractor);

        var tgId = getTgUserIdPort.getTgUserId(uidExtractor.getUserId());

        context.sendMessageBuilder.chatId(tgId);

        var renderer = new ContentRenderer(context);

        messageContent.accept(renderer);

        var tgMessage = context.sendMessageBuilder.build();

        try {
            var res = client.execute(tgMessage);

            tgMessageLinker.link(messageId, res.getMessageId());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}