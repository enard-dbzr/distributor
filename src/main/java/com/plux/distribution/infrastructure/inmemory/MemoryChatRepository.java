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
    public @NotNull Chat create() {
        var newChat = new Chat(new ChatId((long) chats.size()), null);
        chats.add(newChat);
        return newChat;
    }
}
