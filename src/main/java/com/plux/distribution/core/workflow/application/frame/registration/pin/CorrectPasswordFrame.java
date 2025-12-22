package com.plux.distribution.core.workflow.application.frame.registration.pin;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CorrectPasswordFrame implements Frame {

    @Override
    public void onEnter() {
        context.send(new SimpleMessageContent(
                context.getTextProvider().getString("registration.password.correct"),
                List.of()
        ));
        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameFeedback feedback) {

    }
}
