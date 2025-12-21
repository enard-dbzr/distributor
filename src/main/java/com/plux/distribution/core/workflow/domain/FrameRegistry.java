package com.plux.distribution.core.workflow.domain;

import org.jetbrains.annotations.NotNull;

public interface FrameRegistry {

    String getFrameId(@NotNull Class<? extends Frame> frameType);

    @NotNull FrameFactory<? extends Frame> getFactory(@NotNull String frameId);
}
