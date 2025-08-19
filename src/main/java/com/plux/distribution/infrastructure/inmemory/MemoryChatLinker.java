package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.port.exception.ChatIdNotFound;
import com.plux.distribution.application.port.out.specific.telegram.chat.GetTgChatIdPort;
import com.plux.distribution.application.port.out.specific.telegram.chat.GetChatIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.chat.TgChatLinker;
import com.plux.distribution.domain.chat.ChatId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public class MemoryChatLinker implements GetTgChatIdPort, GetChatIdByTgPort, TgChatLinker {
    Map<Long, ChatId> tgToChatId = new ConcurrentHashMap<>();
    Map<ChatId, Long> chatToTg = new ConcurrentHashMap<>();

    @Override
    public @NotNull Long getTgChatId(@NotNull ChatId chatId) {
        return chatToTg.get(chatId);
    }

    @Override
    public @NotNull ChatId getChatId(@NotNull Long tgChatId) throws ChatIdNotFound {
        if (!tgToChatId.containsKey(tgChatId)) {
            throw new ChatIdNotFound("ChatId not found");
        }

        return tgToChatId.get(tgChatId);
    }

    @Override
    public void link(@NotNull ChatId internal, @NotNull Long external) {
        tgToChatId.put(external, internal);
        chatToTg.put(internal, external);
    }
}
