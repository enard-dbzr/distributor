package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.port.out.specific.telegram.message.GetMessageIdByTgPort;
import com.plux.distribution.application.port.out.specific.telegram.message.GetTgMessageIdPort;
import com.plux.distribution.application.port.out.specific.telegram.message.TgMessageGlobalId;
import com.plux.distribution.application.port.out.specific.telegram.message.TgMessageLinker;
import com.plux.distribution.domain.message.MessageId;
import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class MemoryMessageLinker implements TgMessageLinker, GetMessageIdByTgPort, GetTgMessageIdPort {
    private final Map<TgMessageGlobalId, MessageId> forwardMap = new LinkedHashMap<>();
    private final Map<MessageId, TgMessageGlobalId> backwardMap = new LinkedHashMap<>();


    @Override
    public @NotNull MessageId getMessageId(TgMessageGlobalId messageGlobalId) {
        var value = forwardMap.get(messageGlobalId);
        return value != null ? value : new MessageId(1L);
    }

    @Override
    public void link(@NotNull MessageId internal, @NotNull TgMessageGlobalId external) {
        forwardMap.put(external, internal);
        backwardMap.put(internal, external);
    }

    @Override
    public TgMessageGlobalId getTgMessageId(MessageId messageId) {
        return backwardMap.get(messageId);
    }
}
