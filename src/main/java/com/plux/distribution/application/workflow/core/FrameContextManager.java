package com.plux.distribution.application.workflow.core;

import com.plux.distribution.domain.action.ChatAction;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;

public interface FrameContextManager {
    MessageId send(FrameContext context, Frame frame, Message message);
    void dispatch(FrameContext context, ChatAction action);
}
