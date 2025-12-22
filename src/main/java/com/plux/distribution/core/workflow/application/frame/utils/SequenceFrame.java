package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.frame.AbstractFrame;
import com.plux.distribution.core.workflow.domain.frame.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.FrameFeedback;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class SequenceFrame extends AbstractFrame {

    private final List<Frame> frames;

    public SequenceFrame(List<Frame> frames) {
        this.frames = new ArrayList<>(frames);
    }

    @Override
    public void onEnter(@NotNull FrameContext context) {
        frames.getFirst().onEnter(context);

        changeStateAttempt(context);
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        frames.getFirst().handle(context, feedback);

        changeStateAttempt(context);
    }

    @Override
    public void onExit(@NotNull FrameContext context) {
        if (frames.isEmpty()) {
            return;
        }

        frames.getFirst().onExit(context);
    }

    public void changeStateAttempt(@NotNull FrameContext context) {
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

    public static class SequenceFrameFactory extends PoolAwareSerializer<SequenceFrame> {

        @Override
        public PoolNodeSnapshotBuilder buildSnapshot(@NotNull FrameContext context, SequenceFrame instance,
                PoolNodeSnapshotBuilder builder) {
            var framePoolIds = instance.frames.stream()
                    .map(f -> context.getObjectPool().put(context, f))
                    .toList();

            var subNodeBuilder = PoolNodeSnapshot.builder();
            for (int i = 0; i < framePoolIds.size(); i++) {
                subNodeBuilder.value(String.valueOf(i), framePoolIds.get(i));
            }

            return super.buildSnapshot(context, instance, builder)
                    .child("pooledFrames", subNodeBuilder.build());
        }

        @Override
        public SequenceFrame create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            var framePoolIds = snapshot.children().get("pooledFrames").values().entrySet().stream()
                    .map(e -> Map.entry(Integer.parseInt(e.getKey()), e.getValue()))
                    .sorted(Comparator.comparingInt(Map.Entry::getKey))
                    .map(Entry::getValue)
                    .toList();

            var frames = framePoolIds.stream()
                    .map(poolId -> context.getObjectPool().getData(context, poolId, Frame.class))
                    .collect(Collectors.toList());

            return new SequenceFrame(frames);
        }
    }
}
