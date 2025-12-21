package com.plux.distribution.core.workflow.domain;

import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;

public interface FrameContextManager {

    InteractionId send(FrameContext context, InteractionContent message);

    void dispatch(FrameContext context, ChatAction action);
}
