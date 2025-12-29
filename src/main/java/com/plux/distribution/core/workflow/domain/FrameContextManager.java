package com.plux.distribution.core.workflow.domain;

import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import org.jetbrains.annotations.NotNull;

public interface FrameContextManager {

    @NotNull InteractionId send(@NotNull FrameContext context, @NotNull InteractionContent message);

    void dispatch(@NotNull FrameContext context, @NotNull ChatAction action);
}
