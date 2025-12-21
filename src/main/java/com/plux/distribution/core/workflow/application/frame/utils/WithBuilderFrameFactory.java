package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFactory;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import org.jetbrains.annotations.NotNull;

public abstract class WithBuilderFrameFactory<T extends Frame> implements FrameFactory<T> {

    @Override
    public final @NotNull FrameSnapshot save(@NotNull FrameContext context, @NotNull T frame) {
        var builder = new FrameSnapshotBuilder();
        builder.setFrame(context.getObjectPool().put(context, frame));

        return buildSnapshot(context, frame, builder).createFrameSnapshot();
    }

    protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context, @NotNull T frame,
            @NotNull FrameSnapshotBuilder builder) {
        return builder;
    }

    @Override
    public abstract @NotNull T create(@NotNull FrameContext context, @NotNull FrameSnapshot snapshot);
}
