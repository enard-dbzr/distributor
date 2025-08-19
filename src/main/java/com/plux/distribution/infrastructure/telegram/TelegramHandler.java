package com.plux.distribution.infrastructure.telegram;

import com.plux.distribution.application.port.in.RegisterFeedbackUseCase;
import com.plux.distribution.application.port.in.context.ButtonContext;
import com.plux.distribution.application.port.in.context.MessageContext;
import com.plux.distribution.application.port.exception.ChatIdNotFound;
import com.plux.distribution.application.port.out.specific.telegram.message.GetMessageIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.chat.GetChatIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.message.TgMessageGlobalId;
import com.plux.distribution.application.port.out.specific.telegram.message.TgMessageLinker;
import com.plux.distribution.application.port.out.specific.telegram.chat.TgChatLinker;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.chat.ChatId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class TelegramHandler implements LongPollingSingleThreadUpdateConsumer {

    private final @NotNull RegisterFeedbackUseCase registerFeedbackUseCase;
    private final @NotNull GetMessageIdByTgPort getMessageIdByTgPort;
    private final @NotNull TgMessageLinker messageLinker;
    private final @NotNull GetChatIdByTgPort getChatIdByTgPort;
    private final @NotNull TgChatLinker tgChatLinker;

    public TelegramHandler(@NotNull RegisterFeedbackUseCase registerFeedbackUseCase,
            @NotNull GetMessageIdByTgPort getMessageIdByTgPort,
            @NotNull TgMessageLinker messageLinker,
            @NotNull GetChatIdByTgPort getChatIdByTgPort, @NotNull TgChatLinker tgChatLinker) {
        this.registerFeedbackUseCase = registerFeedbackUseCase;
        this.getMessageIdByTgPort = getMessageIdByTgPort;
        this.messageLinker = messageLinker;
        this.getChatIdByTgPort = getChatIdByTgPort;
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
        var data_context = new MessageContext() {
            @Override
            public @NotNull ChatId getChatId() throws ChatIdNotFound {
                return getChatIdByTgPort.getChatId(message.getChatId());
            }

            @Override
            public MessageId getReplyTo() {
                if (message.isReply()) {
                    return getMessageIdByTgPort.getMessageId(
                            new TgMessageGlobalId(
                                    message.getReplyToMessage().getMessageId(),
                                    message.getChatId()
                            )
                    );
                }

                return null;
            }

            @Override
            public @NotNull String getText() {
                return message.getText();
            }

            @Override
            public @NotNull Date getTimestamp() {
                return new Date(message.getDate() * 1000L);
            }

            @Override
            public void onMessageCreated(@NotNull MessageId messageId) {
                messageLinker.link(messageId, new TgMessageGlobalId(message.getMessageId(), message.getChatId()));
            }

            @Override
            public void onChatCreated(@NotNull ChatId chatId) {
                tgChatLinker.link(chatId, message.getChatId());
            }
        };

        registerFeedbackUseCase.handle_message(data_context);
    }

    private void processCallback(CallbackQuery callbackQuery) {
        var tag = callbackQuery.getData();
        var tgChatId = callbackQuery.getMessage().getChatId();
        var tgMessageId = callbackQuery.getMessage().getMessageId();

        var data_context = new ButtonContext() {
            @Override
            public @NotNull ChatId getChatId() throws ChatIdNotFound {
                return getChatIdByTgPort.getChatId(tgChatId);
            }

            @Override
            public @NotNull MessageId getReplyTo() {
                return getMessageIdByTgPort.getMessageId(new TgMessageGlobalId(tgMessageId, tgChatId));
            }

            @Override
            public @NotNull String getTag() {
                return tag;
            }
        };

        registerFeedbackUseCase.handle_button(data_context);
    }
}
