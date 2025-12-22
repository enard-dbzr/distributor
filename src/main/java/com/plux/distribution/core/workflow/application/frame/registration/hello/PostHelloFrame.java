package com.plux.distribution.core.workflow.application.frame.registration.hello;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class PostHelloFrame implements Frame {

    @Override
    public void onEnter() {
        context.send(new SimpleMessageContent(
                context.getTextProvider().getString("registration.start.confirmed"),
                List.of()
        ));

        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameFeedback feedback) {

    }
}
