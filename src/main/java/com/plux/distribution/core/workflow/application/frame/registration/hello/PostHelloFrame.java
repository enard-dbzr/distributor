package com.plux.distribution.core.workflow.application.frame.registration.hello;

import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class PostHelloFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        context.send(new SimpleMessageContent(
                context.getTextProvider().getString("registration.start.confirmed"),
                List.of()
        ));

        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
