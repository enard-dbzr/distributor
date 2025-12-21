package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFactory;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.PoolId;
import java.util.Map;
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
                currentState = null;
            }
        }
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        if (currentState != null) {
            currentState.handle(context, feedback);

            if (currentState.isFinished()) {
                currentState = null;
            }
        }
    }

    public static class RootFrameFactory extends WithBuilderFrameFactory<RootFrame> {

        @Override
        protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context, @NotNull RootFrame frame,
                @NotNull FrameSnapshotBuilder builder) {
            return super.buildSnapshot(context, frame, builder)
                    .addData("currentFrame", context.getObjectPool().put(context, frame.currentState));
        }

        @Override
        public @NotNull RootFrame create(@NotNull FrameContext context, @NotNull FrameSnapshot snapshot) {
            return new RootFrame(
                    context.getObjectPool().getData(
                            context,
                            snapshot.data().get("currentFrame"),
                            Frame.class
                    )
            );
        }
    }
}
