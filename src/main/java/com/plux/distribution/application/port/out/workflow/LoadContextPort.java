package com.plux.distribution.application.port.out.workflow;

import com.plux.distribution.application.workflow.core.FrameContextManager;
import com.plux.distribution.application.workflow.core.FrameFactory;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.application.workflow.core.FrameContext;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface LoadContextPort {
    @NotNull Optional<FrameContext> load(@NotNull ChatId chatId, @NotNull FrameContextManager manager,
            @NotNull FrameFactory frameFactory);
}
