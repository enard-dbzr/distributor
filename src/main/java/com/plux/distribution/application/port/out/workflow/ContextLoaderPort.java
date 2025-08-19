package com.plux.distribution.application.port.out.workflow;

import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.application.workflow.core.FrameContext;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface ContextLoaderPort {
    @NotNull Optional<FrameContext> load(@NotNull ChatId chatId);

    @NotNull FrameContext init(ChatId chatId);
}
