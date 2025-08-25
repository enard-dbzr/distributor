package com.plux.distribution.application.workflow.core;

import com.plux.distribution.domain.action.ChatAction;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.content.MessageContent;

public interface FrameContextManager {
    MessageId send(FrameContext context, Frame frame, MessageContent message);
    void dispatch(FrameContext context, ChatAction action);
}
