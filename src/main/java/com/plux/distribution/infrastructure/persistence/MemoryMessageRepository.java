package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.message.CreateMessagePort;
import com.plux.distribution.application.port.out.message.UpdateMessagePort;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class MemoryMessageRepository implements CreateMessagePort, UpdateMessagePort {
    private final List<Message> messages = new ArrayList<>();

    @Override
    public @NotNull MessageId create(@NotNull Message message) {
        messages.add(message);
        return new MessageId((long) (messages.size() - 1));
    }

    @Override
    public void update(@NotNull MessageId id, @NotNull Message message) {
        messages.set(Math.toIntExact(id.value()), message);
    }
}
