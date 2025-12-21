package com.plux.distribution.core.workflow.domain;

public abstract class AbstractFrame implements Frame {

    private boolean finished = false;

    @Override
    public final boolean isFinished() {
        return finished;
    }

    protected final void markFinished() {
        finished = true;
    }
}
