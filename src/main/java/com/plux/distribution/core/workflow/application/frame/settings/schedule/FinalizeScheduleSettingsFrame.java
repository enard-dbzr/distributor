package com.plux.distribution.core.workflow.application.frame.settings.schedule;

import com.plux.distribution.core.session.application.port.in.SetScheduleSettingsUseCase;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import org.jetbrains.annotations.NotNull;

public class FinalizeScheduleSettingsFrame implements Frame {

    private final SetScheduleSettingsUseCase setScheduleSettingsUseCase;

    public FinalizeScheduleSettingsFrame(SetScheduleSettingsUseCase setScheduleSettingsUseCase) {
        this.setScheduleSettingsUseCase = setScheduleSettingsUseCase;
    }

    @Override
    public void onEnter() {
        var settingsBuilder = context.getData().get(ScheduleSettingsBuilder.class);

        setScheduleSettingsUseCase.set(context.getChatId(), settingsBuilder.buildSettings());

        context.getData().remove(ScheduleSettingsBuilder.class);

        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameFeedback feedback) {

    }
}
