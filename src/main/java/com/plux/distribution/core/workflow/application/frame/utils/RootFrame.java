package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFactory;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.PoolId;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RootFrame extends AbstractFrame {

    private @Nullable Frame currentState;

    public RootFrame(@NotNull FrameContext context) {
        super(context);
    }

    @Override
    public void changeState(@Nullable Frame nextFrame) {
        if (currentState != null) {
            currentState.onExit();
        }
        currentState = nextFrame;
        if (currentState != null) {
            currentState.onEnter();
        }
    }

    @Override
    public void handle(@NotNull FrameFeedback feedback) {
        if (currentState != null) {
            currentState.handle(feedback);
        }
    }

    public static class RootFrameFactory implements FrameFactory<RootFrame> {


        @Override
        public @NotNull FrameSnapshot save(@NotNull FrameContext context, @NotNull RootFrame frame) {
            Map<String, PoolId> data = frame.currentState == null
                    ? Map.of()
                    : Map.of("currentFrame", context.getObjectPool().put(context, frame.currentState));

            return new FrameSnapshot(
                    context.getObjectPool().put(context, frame),
                    data,
                    Map.of()
            );
        }

        @Override
        public @NotNull RootFrame create(@NotNull FrameContext context) {
            return new RootFrame(context);
        }

        @Override
        public void restore(@NotNull FrameContext context, @NotNull RootFrame instance,
                @NotNull FrameSnapshot snapshot) {
            instance.currentState = context.getObjectPool().getData(
                    context,
                    snapshot.data().get("currentFrame"),
                    Frame.class
            );
        }
    }
}
