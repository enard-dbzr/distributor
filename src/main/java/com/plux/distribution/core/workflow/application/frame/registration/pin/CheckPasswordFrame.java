package com.plux.distribution.core.workflow.application.frame.registration.pin;

import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CheckPasswordFrame implements Frame {

    private final String password;

    public CheckPasswordFrame(String password) {
        this.password = password;
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var message = new SimpleMessageContent("Для продолжения необходимо ввести пароль", List.of());

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
