package com.plux.distribution.infrastructure.telegram;

import com.plux.distribution.application.port.in.RegisterFeedbackUseCase;
import com.plux.distribution.application.port.in.dto.ButtonData;
import com.plux.distribution.application.port.in.dto.MessageData;
import com.plux.distribution.application.port.out.specific.telegram.GetMessageIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.GetUserIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.TgMessageLinker;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramHandler implements LongPollingSingleThreadUpdateConsumer {

    private final @NotNull RegisterFeedbackUseCase registerFeedbackUseCase;
    private final @NotNull GetMessageIdByTgPort getMessageIdByTgPort;
    private final @NotNull TgMessageLinker messageLinker;
    private final @NotNull GetUserIdByTgPort getUserIdByTgPort;

    public TelegramHandler(@NotNull RegisterFeedbackUseCase registerFeedbackUseCase,
            @NotNull GetMessageIdByTgPort getMessageIdByTgPort,
            @NotNull TgMessageLinker messageLinker,
            @NotNull GetUserIdByTgPort getUserIdByTgPort) {
        this.registerFeedbackUseCase = registerFeedbackUseCase;
        this.getMessageIdByTgPort = getMessageIdByTgPort;
        this.messageLinker = messageLinker;
        this.getUserIdByTgPort = getUserIdByTgPort;
    }


    @Override
    public void consume(Update update) {

        if (update.hasMessage()) {
            var message = update.getMessage();
            var builder = MessageData.builder();

            builder.setText(message.getText());
            builder.setUserId(getUserIdByTgPort.getUserId(message.getChatId()));
            builder.setTimestamp(new Date(message.getDate() * 1000L));

            if (message.isReply()) {
                builder.setReplyTo(
                        getMessageIdByTgPort.getMessageId(
                                message.getReplyToMessage().getMessageId()
                        )
                );
            }

            var userMessageId = registerFeedbackUseCase.handle_message(builder.build());
            messageLinker.link(userMessageId, message.getMessageId());

            return;
        }

        if (update.hasCallbackQuery()) {
            var tag = update.getCallbackQuery().getData();
            var tgUserId = update.getCallbackQuery().getMessage().getChatId();
            var tgMessageId = update.getCallbackQuery().getMessage().getMessageId();

            var data = ButtonData
                    .builder()
                    .setUserId(getUserIdByTgPort.getUserId(tgUserId))
                    .setReplyTo(getMessageIdByTgPort.getMessageId(tgMessageId))
                    .setTag(tag)
                    .build();

            registerFeedbackUseCase.handle_button(data);
        }
    }
}
