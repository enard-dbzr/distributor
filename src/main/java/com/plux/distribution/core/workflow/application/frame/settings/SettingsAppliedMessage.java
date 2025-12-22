package com.plux.distribution.core.workflow.application.frame.settings;

import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import org.jetbrains.annotations.NotNull;

public class SettingsAppliedMessage implements Frame {

    @Override
    public void onEnter() {
        var message = new InfoMessageFrame(context.getTextProvider().getString("settings.success"));

        context.changeState(message);
    }

    @Override
    public void handle(@NotNull FrameFeedback feedback) {

    }
}
