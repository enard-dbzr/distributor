package com.plux.distribution.infrastructure.telegram;

import com.plux.distribution.core.message.application.port.out.ActionExecutorPort;
import com.plux.distribution.infrastructure.telegram.port.chat.GetTgChatIdPort;
import com.plux.distribution.infrastructure.telegram.port.message.GetTgMessageIdPort;
import com.plux.distribution.core.message.application.dto.action.ChatAction;
import com.plux.distribution.core.message.application.dto.action.ChatActionVisitor;
import com.plux.distribution.core.message.application.dto.action.ClearButtonsAction;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramActionExecutor implements ActionExecutorPort {
    private final @NotNull TelegramClient client;
    private final @NotNull GetTgChatIdPort getTgChatIdPort;
    private final @NotNull GetTgMessageIdPort getTgMessageIdPort;

    public TelegramActionExecutor(@NotNull TelegramClient client, @NotNull GetTgChatIdPort getTgChatIdPort,
            @NotNull GetTgMessageIdPort getTgMessageIdPort) {
        this.client = client;
        this.getTgChatIdPort = getTgChatIdPort;
        this.getTgMessageIdPort = getTgMessageIdPort;
    }

    @Override
    public void execute(@NotNull ChatAction chatAction) {
        // TODO: убрать зависсимость от ChatAction в связи с возможным SendMessageAction
        var tgChatId = getTgChatIdPort.getTgChatId(chatAction.getChatId());

        //noinspection Convert2Lambda
        chatAction.accept(new ChatActionVisitor() {
            @Override
            public void visit(ClearButtonsAction entity) {
                var tgMessageId = getTgMessageIdPort.getTgMessageId(entity.getMessageId()).messageId();

                var command = EditMessageReplyMarkup.builder().chatId(tgChatId).messageId(tgMessageId).build();
                try {
                    client.execute(command);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
