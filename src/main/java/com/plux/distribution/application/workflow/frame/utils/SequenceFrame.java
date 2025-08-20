package com.plux.distribution.application.workflow.frame.utils;

import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class SequenceFrame implements Frame {

    private final String name;
    private List<Frame> frames;

    public SequenceFrame(String name, List<Frame> frames) {
        this.name = name;
        this.frames = frames;
    }

    public SequenceFrame(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getKey() {
        return name;
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var frames = getFrames(context);

        context.pop();
        frames.reversed().forEach(frame -> context.push(frame, true));
        context.exec();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }

    protected List<Frame> getFrames(@NotNull FrameContext context) {
        return frames;
    }
}
