package com.plux.distribution.domain.workflow;

import org.jetbrains.annotations.NotNull;

public interface Frame {

    void exec(@NotNull FrameContext context);

    void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback);

}
