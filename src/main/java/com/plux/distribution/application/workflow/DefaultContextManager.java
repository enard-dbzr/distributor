package com.plux.distribution.application.workflow;

import com.plux.distribution.application.port.in.ExecuteActionUseCase;
import com.plux.distribution.application.port.in.MessageDeliveryUseCase;
import com.plux.distribution.domain.action.ChatAction;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameContextManager;
import com.plux.distribution.domain.message.MessageId;
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
    public MessageId send(FrameContext context, Frame frame, Message message) {
        return deliveryUseCase.send(message);
    }

    @Override
    public void dispatch(FrameContext context, ChatAction action) {
        executeActionUseCase.execute(action);
    }
}
