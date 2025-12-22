package com.plux.distribution.core.workflow.domain;

import org.jetbrains.annotations.NotNull;

public class AbstractFrame implements Frame {

    private final @NotNull FrameContext context;

    public AbstractFrame(@NotNull FrameContext context) {
        this.context = context;
    }
}
