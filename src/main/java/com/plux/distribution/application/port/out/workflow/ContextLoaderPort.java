package com.plux.distribution.application.port.out.workflow;

import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.user.UserId;
import com.plux.distribution.application.workflow.core.FrameContext;
import org.jetbrains.annotations.NotNull;

public interface ContextLoaderPort {
    @NotNull FrameContext load(UserId userId, MessageId messageId);

    @NotNull FrameContext init(UserId userId);
}
