package com.plux.distribution.application.workflow;

import com.plux.distribution.application.port.in.MessageDeliveryUseCase;
import com.plux.distribution.application.port.out.workflow.ContextLinkerPort;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameContextManager;

public class DefaultContextManager implements FrameContextManager {
    public final ContextLinkerPort linker;
    public final MessageDeliveryUseCase deliveryUseCase;

    public DefaultContextManager(ContextLinkerPort linker, MessageDeliveryUseCase deliveryUseCase) {
        this.linker = linker;
        this.deliveryUseCase = deliveryUseCase;
    }

    @Override
    public void send(FrameContext context, Frame frame, Message message) {
        var messageId = deliveryUseCase.send(message);

        linker.link(context, messageId);
    }
}
