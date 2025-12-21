package com.plux.distribution.core.workflow.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Frame {

    default void onEnter(@NotNull FrameContext context) {}

    default void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {}

    default void onExit(@NotNull FrameContext context) {}

    boolean isFinished();

}
