package com.plux.distribution.core.workflow.domain.frame;

import com.plux.distribution.core.workflow.domain.FrameContext;
import org.jetbrains.annotations.NotNull;

public interface Frame {

    default void onEnter(@NotNull FrameContext context) {}

    default void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {}

    default void onExit(@NotNull FrameContext context) {}

    boolean isFinished();

}
