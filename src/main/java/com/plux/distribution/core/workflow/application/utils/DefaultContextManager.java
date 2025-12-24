package com.plux.distribution.core.workflow.application.utils;

import com.plux.distribution.core.message.application.command.SendMessageCommand;
import com.plux.distribution.core.message.application.port.in.ExecuteActionUseCase;
import com.plux.distribution.core.message.application.port.in.MessageDeliveryUseCase;
import com.plux.distribution.core.message.application.dto.action.ChatAction;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameContextManager;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.participant.ChatParticipant;
import com.plux.distribution.core.message.domain.participant.SelfParticipant;
import org.jetbrains.annotations.NotNull;

public class DefaultContextManager implements FrameContextManager {
    public final @NotNull MessageDeliveryUseCase deliveryUseCase;
    public final @NotNull ExecuteActionUseCase executeActionUseCase;

    public DefaultContextManager(@NotNull MessageDeliveryUseCase deliveryUseCase,
            @NotNull ExecuteActionUseCase executeActionUseCase) {
        this.deliveryUseCase = deliveryUseCase;
        this.executeActionUseCase = executeActionUseCase;
    }

    @Override
    public MessageId send(FrameContext context, Frame frame, MessageContent message) {
        return deliveryUseCase.send(new SendMessageCommand(
                new SelfParticipant(),
                new ChatParticipant(context.getChatId()),
                message
        ));
    }

    @Override
    public void dispatch(FrameContext context, ChatAction action) {
        executeActionUseCase.execute(action);
    }
}
