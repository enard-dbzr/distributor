package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class SequenceFrame implements Frame {

    private final List<Frame> frames;

    public SequenceFrame(List<Frame> frames) {
        this.frames = frames;
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        context.pop();
        frames.reversed().forEach(frame -> context.push(frame, true));
        context.exec();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
