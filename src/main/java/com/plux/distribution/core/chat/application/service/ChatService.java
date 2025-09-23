package com.plux.distribution.core.chat.application.service;

import com.plux.distribution.core.chat.application.dto.ChatDto;
import com.plux.distribution.core.chat.application.port.in.AssignUserToChatUseCase;
import com.plux.distribution.core.chat.application.port.in.CreateChatUseCase;
import com.plux.distribution.core.chat.application.port.in.GetAllChatsUseCase;
import com.plux.distribution.core.chat.application.port.in.GetChatUseCase;
import com.plux.distribution.core.chat.application.port.out.CreateChatPort;
import com.plux.distribution.core.chat.application.port.out.GetChatPort;
import com.plux.distribution.core.chat.application.port.out.UpdateChatPort;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.user.domain.UserId;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ChatService implements CreateChatUseCase, AssignUserToChatUseCase, GetAllChatsUseCase, GetChatUseCase {

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

    @Override
    public @NotNull List<ChatId> getAllChatIds() {
        return getChatPort.getAllChatIds();
    }

    @Override
    public @NotNull ChatDto get(@NotNull ChatId chatId) {
        var chat = getChatPort.get(chatId);
        return new ChatDto(chat);
    }
}
