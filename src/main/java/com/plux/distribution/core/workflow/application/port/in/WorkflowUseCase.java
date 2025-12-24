package com.plux.distribution.core.workflow.application.port.in;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.workflow.domain.FrameContext;
import org.jetbrains.annotations.NotNull;

public interface WorkflowUseCase {

    void save(@NotNull FrameContext frameContext);

    @NotNull FrameContext load(@NotNull ChatId chatId);

}
