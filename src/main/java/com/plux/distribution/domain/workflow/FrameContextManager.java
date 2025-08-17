package com.plux.distribution.domain.workflow;

import com.plux.distribution.domain.message.Message;

public interface FrameContextManager {
    void send(FrameContext context, Frame frame, Message message);
}
