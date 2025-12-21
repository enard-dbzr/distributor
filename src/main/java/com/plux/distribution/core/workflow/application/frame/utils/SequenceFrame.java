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

public class SequenceFrame extends AbstractFrame {

    private List<Frame> frames;

    public SequenceFrame(List<Frame> frames) {
        this.frames = frames;
    }

    @Override
    public void onEnter(@NotNull FrameContext context) {
        if (frames == null) {
            throw new IllegalStateException("frames is null");
        }

        frames.getFirst().onEnter(context);

        changeStateAttempt(context);
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        if (frames == null) {
            throw new IllegalStateException("frames is null");
        }

        frames.getFirst().handle(context, feedback);

        changeStateAttempt(context);
    }

    @Override
    public void onExit(@NotNull FrameContext context) {
        if (frames == null) {
            throw new IllegalStateException("frames is null");
        }

        if (frames.isEmpty()) {
            return;
        }

        frames.getFirst().onExit(context);
    }

    public void changeStateAttempt(@NotNull FrameContext context) {
        if (frames == null) {
            throw new IllegalStateException("frames is null");
        }

        if (frames.isEmpty()) {
            markFinished();
            return;
        }

        if (frames.getFirst().isFinished()) {
            var current = frames.removeFirst();
            current.onExit(context);

            if (frames.isEmpty()) {
                markFinished();
            } else {
                frames.getFirst().onEnter(context);
            }
        }
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
        public @NotNull SequenceFrame create(@NotNull FrameContext context, @NotNull FrameSnapshot snapshot) {
            @SuppressWarnings("unchecked")
            List<String> pooledFrames = (List<String>) snapshot.deepData().get("pooledFrames");

            var frames = pooledFrames.stream()
                    .map(UUID::fromString)
                    .map(PoolId::new)
                    .map(poolId -> context.getObjectPool().getData(context, poolId, Frame.class))
                    .collect(Collectors.toList());

            return new SequenceFrame(frames);
        }
    }
}
