package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.port.out.specific.telegram.message.GetMessageIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.message.GetTgMessageIdPort;
import com.plux.distribution.application.port.out.specific.telegram.message.TgMessageLinker;
import com.plux.distribution.domain.message.MessageId;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class MemoryMessageLinker implements TgMessageLinker, GetMessageIdByTgPort, GetTgMessageIdPort {
    // FIXME: add chatId to linking key

    private final Map<Integer, MessageId> forwardMap = new LinkedHashMap<>();
    private final Map<MessageId, Integer> backwardMap = new LinkedHashMap<>();


    @Override
    public @NotNull MessageId getMessageId(Integer messageId) {
        var value = forwardMap.get(messageId);
        return value != null ? value : new MessageId(1L);
    }

    @Override
    public void link(@NotNull MessageId internal, @NotNull Integer external) {
        forwardMap.put(external, internal);
        backwardMap.put(internal, external);
    }

    @Override
    public Integer getTgMessageId(MessageId messageId) {
        return backwardMap.get(messageId);
    }
}
