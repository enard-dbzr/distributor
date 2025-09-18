package com.plux.distribution.application.port.in.workflow;

import com.plux.distribution.domain.workflow.FrameContext;
import com.plux.distribution.domain.chat.ChatId;
import org.jetbrains.annotations.NotNull;

public interface WorkflowUseCase {

    void save(@NotNull FrameContext frameContext);

    @NotNull FrameContext load(@NotNull ChatId chatId);

}
