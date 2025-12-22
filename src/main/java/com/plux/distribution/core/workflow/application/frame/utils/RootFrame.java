package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.frame.AbstractFrame;
import com.plux.distribution.core.workflow.domain.frame.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.FrameFeedback;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RootFrame extends AbstractFrame {

    private @Nullable Frame currentState;

    public RootFrame(@Nullable Frame currentState) {
        this.currentState = currentState;
    }

    public void changeState(@NotNull FrameContext context, @Nullable Frame nextFrame) {
        if (currentState != null) {
            currentState.onExit(context);
        }

        currentState = nextFrame;

        if (currentState != null) {
            currentState.onEnter(context);

            if (currentState.isFinished()) {
                currentState.onExit(context);
                currentState = null;
            }
        }
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        if (currentState != null) {
            currentState.handle(context, feedback);

            if (currentState.isFinished()) {
                currentState.onExit(context);
                currentState = null;
            }
        }
    }

    public static class RootFrameFactory extends PoolAwareSerializer<RootFrame> {

        @Override
        public PoolNodeSnapshotBuilder buildSnapshot(@NotNull FrameContext context, RootFrame instance,
                PoolNodeSnapshotBuilder builder) {
            return super.buildSnapshot(context, instance, builder)
                    .value("currentFrame", context.getObjectPool().put(context, instance.currentState));
        }

        @Override
        public RootFrame create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            return new RootFrame(
                    context.getObjectPool().getData(
                            context,
                            snapshot.values().get("currentFrame"),
                            Frame.class
                    )
            );
        }

        @Override
        public @NotNull RootFrame create(@NotNull FrameContext context) {
            return new RootFrame(null);
        }
    }
}
