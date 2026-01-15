package com.plux.distribution.infrastructure.telegram.handler;

import com.plux.distribution.core.aggregator.application.port.in.AddGroupPartUseCase;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class TelegramHandler implements LongPollingSingleThreadUpdateConsumer {

    private final @NotNull UpdateProcessor updateProcessor;
    private final @NotNull AddGroupPartUseCase<Message> addGroupPartUseCase;

    public TelegramHandler(@NotNull UpdateProcessor updateProcessor,
            @NotNull AddGroupPartUseCase<Message> addGroupPartUseCase) {
        this.updateProcessor = updateProcessor;
        this.addGroupPartUseCase = addGroupPartUseCase;
    }


    @Override
    public void consume(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.getMediaGroupId() == null) {
                updateProcessor.processMessageGroup(List.of(message));
            } else {
                String groupKey = "tg:" + message.getChatId() + ":mg:" + message.getMediaGroupId();

                addGroupPartUseCase.addPart(groupKey, message);
            }
        }

        if (update.hasCallbackQuery()) {
            updateProcessor.processCallback(update.getCallbackQuery());
        }
    }
}
