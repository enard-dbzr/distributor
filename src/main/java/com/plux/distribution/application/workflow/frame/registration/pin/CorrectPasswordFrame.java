package com.plux.distribution.application.workflow.frame.registration.pin;

import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.UserParticipant;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CorrectPasswordFrame implements Frame {

    @Override
    public @NotNull String getKey() {
        return "registration.check_pin.correct";
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        context.send(new Message(
                new UserParticipant(context.getUserId()),
                new SimpleMessageContent("Отлично, можем продолжить \n(っ◔◡◔)っ❤", List.of())
        ));
        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
