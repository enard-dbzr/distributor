package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.port.out.chat.CreateChatPort;
import com.plux.distribution.domain.chat.Chat;
import com.plux.distribution.domain.chat.ChatId;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class MemoryChatRepository implements CreateChatPort {
    private final List<Chat> chats = new ArrayList<>();

    @Override
    public @NotNull ChatId create(@NotNull Chat chat) {
        chats.add(chat);
        return new ChatId((long) (chats.size() - 1));
    }
}
