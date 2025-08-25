package com.plux.distribution.application.workflow.frame.registration.pin;

import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CheckPasswordFrame implements Frame {

    private final String password;

    public CheckPasswordFrame(String password) {
        this.password = password;
    }

    @Override
    public @NotNull String getKey() {
        return "registration.check_pin";
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
                context.changeState(context.getFactory().get("registration.check_pin.correct"));
            } else {
                context.changeState(context.getFactory().get("registration.check_pin.incorrect"));
            }
        });
    }
}
