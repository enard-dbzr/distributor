package com.plux.distribution.core.workflow.application.frame.registration;

import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import org.jetbrains.annotations.NotNull;

public class ChangeSettingsMessage implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        var message = new InfoMessageFrame(context.getTextProvider().getString("settings.schedule.start"));

        context.changeState(message);
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
