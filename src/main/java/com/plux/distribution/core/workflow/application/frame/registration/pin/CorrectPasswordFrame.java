package com.plux.distribution.core.workflow.application.frame.registration.pin;

import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CorrectPasswordFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        context.send(new SimpleMessageContent("Отлично, можем продолжить \n(っ◔◡◔)っ❤", List.of()));
        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
