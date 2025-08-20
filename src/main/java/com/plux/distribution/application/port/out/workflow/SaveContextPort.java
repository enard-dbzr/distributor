package com.plux.distribution.application.port.out.workflow;

import com.plux.distribution.application.workflow.core.FrameContext;
import org.jetbrains.annotations.NotNull;

public interface SaveContextPort {
    void save(@NotNull FrameContext context);
}
