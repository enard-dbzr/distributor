package com.plux.distribution.core.workflow.domain;

import org.jetbrains.annotations.NotNull;

public interface Frame {

    default void onEnter() {}

    default void handle(@NotNull FrameFeedback feedback) {}

    default void onExit() {}

}
