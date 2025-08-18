package com.plux.distribution.application.workflow.core;

import com.plux.distribution.domain.message.Message;

public interface FrameContextManager {
    void send(FrameContext context, Frame frame, Message message);
}
