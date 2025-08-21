package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.dto.message.CreateMessageCommand;
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
    public @NotNull Message create(@NotNull CreateMessageCommand command) {
        var message = new Message(
                new MessageId((long) messages.size()),
                command.sender(),
                command.recipient(),
                command.state(),
                command.content()
        );

        messages.add(message);
        return message;
    }

    @Override
    public void update(@NotNull Message message) {
        messages.set(Math.toIntExact(message.getId().value()), message);
    }
}
