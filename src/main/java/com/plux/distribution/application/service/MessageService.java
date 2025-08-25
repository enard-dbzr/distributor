package com.plux.distribution.application.service;

import com.plux.distribution.application.dto.message.CreateMessageCommand;
import com.plux.distribution.application.dto.message.SendMessageCommand;
import com.plux.distribution.application.port.in.message.CreateMessageUseCase;
import com.plux.distribution.application.port.in.message.MessageDeliveryUseCase;
import com.plux.distribution.application.port.out.MessageSenderPort;
import com.plux.distribution.application.port.out.message.CreateMessagePort;
import com.plux.distribution.application.port.out.message.UpdateMessagePort;
import com.plux.distribution.domain.message.MessageId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class MessageService implements MessageDeliveryUseCase, CreateMessageUseCase {

    private final MessageSenderPort messageSenderPort;
    private final CreateMessagePort createMessagePort;
    private final UpdateMessagePort updateMessagePort;

    public MessageService(MessageSenderPort messageSenderPort,
            CreateMessagePort createMessagePort, UpdateMessagePort updateMessagePort) {
        this.messageSenderPort = messageSenderPort;
        this.createMessagePort = createMessagePort;
        this.updateMessagePort = updateMessagePort;
    }


    @Override
    public @NotNull MessageId send(@NotNull SendMessageCommand command) {
        var message = createMessagePort.create(command.toCreateCommand());

        messageSenderPort.send(message.getId(), command.recipient(), command.content());
        message.markSent(new Date());

        updateMessagePort.update(message);

        return message.getId();
    }

    @Override
    public MessageId create(CreateMessageCommand command) {
        var message = createMessagePort.create(command);
        return message.getId();
    }
}
