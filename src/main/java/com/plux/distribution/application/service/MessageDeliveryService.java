package com.plux.distribution.application.service;

import com.plux.distribution.application.port.in.MessageDeliveryUseCase;
import com.plux.distribution.application.port.out.MessageSenderPort;
import com.plux.distribution.application.port.out.message.CreateMessagePort;
import com.plux.distribution.application.port.out.message.UpdateMessagePort;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class MessageDeliveryService implements MessageDeliveryUseCase {
    private final MessageSenderPort messageSenderPort;
    private final CreateMessagePort createMessagePort;
    private final UpdateMessagePort updateMessagePort;

    public MessageDeliveryService(MessageSenderPort messageSenderPort,
            CreateMessagePort createMessagePort, UpdateMessagePort updateMessagePort) {
        this.messageSenderPort = messageSenderPort;
        this.createMessagePort = createMessagePort;
        this.updateMessagePort = updateMessagePort;
    }


    @Override
    public @NotNull MessageId send(@NotNull Message message) {
        var messageId = createMessagePort.create(message);

        messageSenderPort.send(messageId, message.getRecipient(), message.getContent());
        message.markSent(new Date());

        updateMessagePort.update(messageId, message);

        return messageId;
    }
}
