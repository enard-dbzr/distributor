package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PassThroughFrame extends AbstractFrame {
    protected Frame parent = null;

    public PassThroughFrame(@NotNull FrameContext context) {
        super(context);
    }

    public PassThroughFrame(@NotNull FrameContext context, Frame parent) {
        super(context);
        this.parent = parent;
    }

    @Override
    public void changeState(@Nullable Frame nextFrame) {
        if (parent == null) {
            throw new IllegalStateException("Parent must bu not null. Check deserialization process.");
        }

        parent.changeState(nextFrame);
    }
}
