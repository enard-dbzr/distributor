package com.plux.distribution.core.workflow.domain;


import org.jetbrains.annotations.NotNull;

public abstract class AbstractFrame implements Frame {

    protected final @NotNull FrameContext context;

    public AbstractFrame(@NotNull FrameContext context) {
        this.context = context;
    }

    @Override
    public final void changeState() {
        changeState(null);
    }
}
