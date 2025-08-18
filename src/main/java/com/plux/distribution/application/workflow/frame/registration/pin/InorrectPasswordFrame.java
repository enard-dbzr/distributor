package com.plux.distribution.application.workflow.frame.registration.pin;

import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.UserParticipant;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class InorrectPasswordFrame implements Frame {

    @Override
    public @NotNull String getKey() {
        return "registration.check_pin.incorrect";
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var masterFrame = context.getFactory().get("registration.check_pin");

        context.changeState(masterFrame, false);

        context.send(new Message(
                new UserParticipant(context.getUserId()),
                new SimpleMessageContent("Неверный пароль \n(ง ͠▧. ͡▧)ง", List.of())
        ), masterFrame);
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
