package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.specific.telegram.GetMessageIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.TgMessageLinker;
import com.plux.distribution.domain.message.MessageId;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class MemoryMessageLinker implements TgMessageLinker, GetMessageIdByTgPort {

    private final Map<Integer, MessageId> messageIdMap = new LinkedHashMap<>();

    @Override
    public @NotNull MessageId getMessageId(Integer messageId) {
        var value = messageIdMap.get(messageId);
        return value != null ? value : new MessageId(1L);
    }

    @Override
    public void link(@NotNull MessageId internal, @NotNull Integer external) {
        messageIdMap.put(external, internal);
    }
}
