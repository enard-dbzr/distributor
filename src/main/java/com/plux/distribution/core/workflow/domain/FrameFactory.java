package com.plux.distribution.core.workflow.domain;

import org.jetbrains.annotations.NotNull;

public interface FrameFactory<T extends Frame> {

    @NotNull FrameSnapshot save(@NotNull FrameContext context, @NotNull T frame);

    @NotNull T create(@NotNull FrameContext context);

    void restore(@NotNull FrameContext context, @NotNull T instance, @NotNull FrameSnapshot snapshot);
}
