package com.plux.distribution.infrastructure.telegram;

import com.plux.distribution.core.chat.application.port.in.CreateChatUseCase;
import com.plux.distribution.core.interaction.application.command.DeliverInteractionCommand;
import com.plux.distribution.core.interaction.application.port.in.InteractionDeliveryUseCase;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.infrastructure.telegram.port.TgChatLinker;
import com.plux.distribution.infrastructure.telegram.port.TgMessageGlobalId;
import com.plux.distribution.infrastructure.telegram.port.TgMessageLinker;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class TelegramHandler implements LongPollingSingleThreadUpdateConsumer {

    private final @NotNull InteractionDeliveryUseCase interactionDeliveryUseCase;
    private final @NotNull CreateChatUseCase createChatUseCase;
    private final @NotNull TgMessageLinker tgMessageLinker;
    private final @NotNull TgChatLinker tgChatLinker;

    public TelegramHandler(@NotNull InteractionDeliveryUseCase interactionDeliveryUseCase,
            @NotNull CreateChatUseCase createChatUseCase,
            @NotNull TgMessageLinker tgMessageLinker, @NotNull TgChatLinker tgChatLinker) {

        this.interactionDeliveryUseCase = interactionDeliveryUseCase;
        this.createChatUseCase = createChatUseCase;
        this.tgMessageLinker = tgMessageLinker;
        this.tgChatLinker = tgChatLinker;
    }


    @Override
    public void consume(Update update) {

        if (update.hasMessage()) {
            processMessage(update.getMessage());

            return;
        }

        if (update.hasCallbackQuery()) {
            processCallback(update.getCallbackQuery());
        }
    }

    private void processMessage(Message message) {
        var chatId = tgChatLinker.getChatId(message.getChatId());

        if (chatId == null) {
            chatId = createChatUseCase.create().id();

            tgChatLinker.link(chatId, message.getChatId());
        }

        InteractionContent content = new SimpleMessageContent(
                Optional.ofNullable(message.getText()).orElse(""),
                List.of()
        );

        if (message.isReply()) {
            var replyTo = tgMessageLinker.getInteractionId(
                    new TgMessageGlobalId(
                            message.getReplyToMessage().getMessageId(),
                            message.getChatId()
                    )
            );

            content = new ReplyMessageContent(content, replyTo);
        }

        var command = new DeliverInteractionCommand(
                new ChatParticipant(chatId),
                new BotParticipant(),
                content
        );

        var messageId = interactionDeliveryUseCase.deliver(command);

        tgMessageLinker.link(messageId, new TgMessageGlobalId(message.getMessageId(), message.getChatId()));
    }

    private void processCallback(CallbackQuery callbackQuery) {
        var tag = callbackQuery.getData();

        var tgChatId = callbackQuery.getMessage().getChatId();
        var tgMessageId = callbackQuery.getMessage().getMessageId();

        var chatId = Optional.ofNullable(tgChatLinker.getChatId(tgChatId)).orElseThrow();
        var interactionId = tgMessageLinker.getInteractionId(new TgMessageGlobalId(tgMessageId, tgChatId));

        interactionDeliveryUseCase.deliver(new DeliverInteractionCommand(
                new ChatParticipant(chatId),
                new BotParticipant(),
                new ButtonClickContent(interactionId, tag)
        ));
    }
}
