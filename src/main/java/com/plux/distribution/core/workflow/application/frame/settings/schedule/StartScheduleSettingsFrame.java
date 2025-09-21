package com.plux.distribution.core.workflow.application.frame.settings.schedule;

import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class StartScheduleSettingsFrame implements Frame {

    private final Frame finalizeFrame;

    public StartScheduleSettingsFrame(Frame finalizeFrame) {
        this.finalizeFrame = finalizeFrame;
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        context.getData().put(ScheduleSettingsBuilder.class, new ScheduleSettingsBuilder());

        var master = new SequenceFrame(List.of(
                new AskTimezoneFrame(),
                new AskHoursFrame(),
                finalizeFrame
        ));

        master.exec(context);
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
