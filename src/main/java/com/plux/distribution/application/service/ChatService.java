package com.plux.distribution.application.service;

import com.plux.distribution.application.dto.chat.ChatDto;
import com.plux.distribution.application.port.in.chat.AssignUserToChatUseCase;
import com.plux.distribution.application.port.in.chat.CreateChatUseCase;
import com.plux.distribution.application.port.out.chat.CreateChatPort;
import com.plux.distribution.application.port.out.chat.GetChatPort;
import com.plux.distribution.application.port.out.chat.UpdateChatPort;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public class ChatService implements CreateChatUseCase, AssignUserToChatUseCase {
    private final @NotNull CreateChatPort createChatPort;
    private final @NotNull GetChatPort getChatPort;
    private final @NotNull UpdateChatPort updateChatPort;

    public ChatService(@NotNull CreateChatPort createChatPort, @NotNull GetChatPort getChatPort,
            @NotNull UpdateChatPort updateChatPort) {
        this.createChatPort = createChatPort;
        this.getChatPort = getChatPort;
        this.updateChatPort = updateChatPort;
    }

    @Override
    public void assignUser(@NotNull ChatId chatId, @NotNull UserId userId) {
        var chat = getChatPort.get(chatId);
        chat.setUserId(userId);
        updateChatPort.update(chat);
    }

    @Override
    public @NotNull ChatDto create() {
        return new ChatDto(createChatPort.create());
    }
}
