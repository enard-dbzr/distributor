package com.plux.distribution.core.workflow.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Frame {

    default void onEnter() {}

    default void handle(@NotNull FrameFeedback feedback) {}

    default void onExit() {}

    void changeState(@Nullable Frame nextFrame);

    void changeState();

}
