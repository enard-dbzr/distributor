package com.plux.distribution.application.workflow.frame.registration.user;

import com.plux.distribution.domain.workflow.Frame;
import com.plux.distribution.domain.workflow.FrameContext;
import com.plux.distribution.domain.workflow.FrameFeedback;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskNameFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        context.send(new SimpleMessageContent("Как тебя зовут? 😊", List.of()));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        var userBuilder = context.getData().get(UserBuilder.class);

        feedback.text().ifPresent(text -> {
            userBuilder.setName(text);
            context.changeState();
        });
    }
}
