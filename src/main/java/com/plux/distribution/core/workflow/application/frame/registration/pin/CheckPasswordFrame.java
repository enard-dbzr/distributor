package com.plux.distribution.core.workflow.application.frame.registration.pin;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CheckPasswordFrame implements Frame {

    private final String password;

    public CheckPasswordFrame(String password) {
        this.password = password;
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var message = new SimpleMessageContent(
                context.getTextProvider().getString("registration.password.ask_message"),
                List.of()
        );

        context.send(message);
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        feedback.text().ifPresent(text -> {
            if (text.equals(password)) {
                context.changeState(new CorrectPasswordFrame(), true);
            } else {
                context.changeState(this, false);
                context.push(new InorrectPasswordFrame(), true);
                context.exec();
            }
        });
    }
}
