package com.plux.distribution.core.workflow.domain;

import org.jetbrains.annotations.NotNull;

public interface Frame {

    void exec(@NotNull FrameContext context);

    void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback);

}
