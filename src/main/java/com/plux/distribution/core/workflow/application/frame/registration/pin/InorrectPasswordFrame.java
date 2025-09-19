package com.plux.distribution.core.workflow.application.frame.registration.pin;

import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class InorrectPasswordFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        context.changeState();

        context.send(new SimpleMessageContent("Неверный пароль \n(ง ͠▧. ͡▧)ง", List.of()));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
