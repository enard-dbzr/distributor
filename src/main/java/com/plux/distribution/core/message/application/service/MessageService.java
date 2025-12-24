package com.plux.distribution.core.message.application.service;

import com.plux.distribution.core.message.application.command.CreateMessageCommand;
import com.plux.distribution.core.message.application.dto.MessageDto;
import com.plux.distribution.core.message.application.command.SendMessageCommand;
import com.plux.distribution.core.message.application.port.in.CreateMessageUseCase;
import com.plux.distribution.core.message.application.port.in.GetMessageUseCase;
import com.plux.distribution.core.message.application.port.in.MessageDeliveryUseCase;
import com.plux.distribution.core.message.application.port.out.MessageSenderPort;
import com.plux.distribution.core.message.application.port.out.CreateMessagePort;
import com.plux.distribution.core.message.application.port.out.GetMessagePort;
import com.plux.distribution.core.message.application.port.out.UpdateMessagePort;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.participant.Participant;
import java.util.Date;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class MessageService implements MessageDeliveryUseCase, CreateMessageUseCase, GetMessageUseCase {

    private final MessageSenderPort messageSenderPort;
    private final CreateMessagePort createMessagePort;
    private final UpdateMessagePort updateMessagePort;
    private final GetMessagePort getMessagePort;

    public MessageService(MessageSenderPort messageSenderPort,
            CreateMessagePort createMessagePort, UpdateMessagePort updateMessagePort, GetMessagePort getMessagePort) {
        this.messageSenderPort = messageSenderPort;
        this.createMessagePort = createMessagePort;
        this.updateMessagePort = updateMessagePort;
        this.getMessagePort = getMessagePort;
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
    public @NotNull MessageDto create(@NotNull CreateMessageCommand command) {
        var message = createMessagePort.create(command);
        return new MessageDto(message);
    }

    @Override
    public @NotNull MessageDto getMessage(@NotNull MessageId messageId) {
        return new MessageDto(getMessagePort.get(messageId));
    }

    @Override
    public Optional<MessageDto> getLastOfRecipient(@NotNull Participant recipient) {
        var message = getMessagePort.getLastOfRecipient(recipient);
        if (message == null) {
            return Optional.empty();
        }

        return Optional.of(new MessageDto(message));
    }
}
