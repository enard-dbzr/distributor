package com.plux.distribution.application.workflow.core;

import org.jetbrains.annotations.NotNull;

public interface Frame {

    @NotNull String getKey();

    void exec(@NotNull FrameContext context);

    void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback);

}
