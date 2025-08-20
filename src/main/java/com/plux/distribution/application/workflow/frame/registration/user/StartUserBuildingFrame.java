package com.plux.distribution.application.workflow.frame.registration.user;

import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.frame.utils.SequenceFrame;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class StartUserBuildingFrame extends SequenceFrame {


    public StartUserBuildingFrame() {
        super("registration.user.start_building");
    }

    @Override
    protected List<Frame> getFrames(@NotNull FrameContext context) {
        context.getData().put(UserBuilder.class, new UserBuilder());

        return List.of(
                new AskNameFrame(),
                new AskEmailFrame(),
                new AskAgeFrame(),
                new AskCityFrame(),
                new AskHobbyFrame(),
                context.getFactory().get("registration.user.finalize")
        );
    }
}
