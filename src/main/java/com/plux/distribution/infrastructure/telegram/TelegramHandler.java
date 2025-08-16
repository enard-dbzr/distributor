package com.plux.distribution.infrastructure.telegram;

import com.plux.distribution.application.port.in.RegisterFeedbackUseCase;
import com.plux.distribution.application.port.in.context.ButtonContext;
import com.plux.distribution.application.port.in.context.MessageContext;
import com.plux.distribution.application.port.exception.UserIdNotFound;
import com.plux.distribution.application.port.out.specific.telegram.GetMessageIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.GetUserIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.TgMessageLinker;
import com.plux.distribution.application.port.out.specific.telegram.TgUserLinker;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.user.UserId;
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
    private final @NotNull GetUserIdByTgPort getUserIdByTgPort;
    private final @NotNull TgUserLinker tgUserLinker;

    public TelegramHandler(@NotNull RegisterFeedbackUseCase registerFeedbackUseCase,
            @NotNull GetMessageIdByTgPort getMessageIdByTgPort,
            @NotNull TgMessageLinker messageLinker,
            @NotNull GetUserIdByTgPort getUserIdByTgPort, @NotNull TgUserLinker tgUserLinker) {
        this.registerFeedbackUseCase = registerFeedbackUseCase;
        this.getMessageIdByTgPort = getMessageIdByTgPort;
        this.messageLinker = messageLinker;
        this.getUserIdByTgPort = getUserIdByTgPort;
        this.tgUserLinker = tgUserLinker;
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
            public @NotNull UserId getUserId() throws UserIdNotFound {
                return getUserIdByTgPort.getUserId(message.getChatId());
            }

            @Override
            public MessageId getReplyTo() {
                if (message.isReply()) {
                    return getMessageIdByTgPort.getMessageId(
                            message.getReplyToMessage().getMessageId()
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
                messageLinker.link(messageId, message.getMessageId());
            }

            @Override
            public void onUserCreated(@NotNull UserId userId) {
                tgUserLinker.link(userId, message.getChatId());
            }
        };

        registerFeedbackUseCase.handle_message(data_context);
    }

    private void processCallback(CallbackQuery callbackQuery) {
        var tag = callbackQuery.getData();
        var tgUserId = callbackQuery.getMessage().getChatId();
        var tgMessageId = callbackQuery.getMessage().getMessageId();

        var data_context = new ButtonContext() {
            @Override
            public @NotNull UserId getUserId() throws UserIdNotFound {
                return getUserIdByTgPort.getUserId(tgUserId);
            }

            @Override
            public @NotNull MessageId getReplyTo() {
                return getMessageIdByTgPort.getMessageId(tgMessageId);
            }

            @Override
            public @NotNull String getTag() {
                return tag;
            }
        };

        registerFeedbackUseCase.handle_button(data_context);
    }
}
