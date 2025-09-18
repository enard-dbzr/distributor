package com.plux.distribution.application.workflow.frame.registration.pin;

import com.plux.distribution.domain.workflow.Frame;
import com.plux.distribution.domain.workflow.FrameContext;
import com.plux.distribution.domain.workflow.FrameFeedback;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
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
