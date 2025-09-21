package com.plux.distribution.core.workflow.application.frame.registration.user;

import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class StartUserBuildingFrame implements Frame {
    private final Frame finalizeFrame;

    public StartUserBuildingFrame(Frame finalizeFrame) {
        this.finalizeFrame = finalizeFrame;
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        context.getData().put(UserBuilder.class, new UserBuilder());

        var master = new SequenceFrame(List.of(
                new AskNameFrame(),
                new AskAgeFrame(),
                new AskCityFrame(),
                new AskHobbyFrame(),
                finalizeFrame
        ));

        master.exec(context);
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
