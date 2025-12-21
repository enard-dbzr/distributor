package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.PoolId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SequenceFrame extends PassThroughFrame {

    private List<Frame> frames;

    public SequenceFrame(@NotNull FrameContext context) {
        super(context);
    }

    public SequenceFrame(@NotNull FrameContext context, Frame parent) {
        super(context, parent);
    }

    @Override
    public void onEnter() {
        if (frames == null) {
            throw new IllegalStateException("frames is null");
        }

        frames.getFirst().onEnter();
    }

    @Override
    public void handle(@NotNull FrameFeedback feedback) {
        if (frames == null) {
            throw new IllegalStateException("frames is null");
        }

        frames.getFirst().handle(feedback);
    }

    @Override
    public void onExit() {
        if (frames == null) {
            throw new IllegalStateException("frames is null");
        }

        if (frames.isEmpty()) {
            return;
        }

        frames.getFirst().onExit();
    }

    @Override
    public void changeState(@Nullable Frame nextFrame) {
        if (frames == null) {
            throw new IllegalStateException("frames is null");
        }

        if (frames.isEmpty()) {
            parent.changeState(nextFrame);
        }

        var current = frames.removeFirst();
        current.onExit();

        if (nextFrame == null) {
            if (frames.isEmpty()) {
                parent.changeState();
            } else {
                frames.getFirst().onEnter();
            }
        } else {
            frames.addFirst(nextFrame);
            frames.getFirst().onEnter();
        }
    }

    public void setFrames(List<Frame> frames) {
        this.frames = frames;
    }

    public static class SequenceFrameFactory extends WithBuilderFrameFactory<SequenceFrame> {

        @Override
        protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context,
                @NotNull SequenceFrame frame, @NotNull FrameSnapshotBuilder builder) {
            var pooledFrames = frame.frames.stream()
                    .map(f -> context.getObjectPool().put(context, f).uuid().toString())
                    .toList();
            return super.buildSnapshot(context, frame, builder).addDeepData("pooledFrames", pooledFrames);
        }

        @Override
        public @NotNull SequenceFrame create(@NotNull FrameContext context) {
            return new SequenceFrame(context);
        }

        @Override
        public void restore(@NotNull FrameContext context, @NotNull SequenceFrame instance,
                @NotNull FrameSnapshot snapshot) {
            super.restore(context, instance, snapshot);

            @SuppressWarnings("unchecked")
            List<String> pooledFrames = (List<String>) snapshot.deepData().get("pooledFrames");

            instance.frames = pooledFrames.stream()
                    .map(UUID::fromString)
                    .map(PoolId::new)
                    .map(poolId -> context.getObjectPool().getData(context, poolId, Frame.class))
                    .collect(Collectors.toList());
        }
    }
}
