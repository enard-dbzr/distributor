package com.plux.distribution.application.port.out.workflow;

import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.application.workflow.core.FrameContext;
import org.jetbrains.annotations.NotNull;

public interface ContextLinkerPort {
    void link(@NotNull FrameContext context, @NotNull MessageId messageId);
}
