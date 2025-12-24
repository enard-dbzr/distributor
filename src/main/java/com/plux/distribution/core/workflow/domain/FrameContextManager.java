package com.plux.distribution.core.workflow.domain;

import com.plux.distribution.core.message.application.dto.action.ChatAction;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.content.MessageContent;

public interface FrameContextManager {
    MessageId send(FrameContext context, Frame frame, MessageContent message);
    void dispatch(FrameContext context, ChatAction action);
}
