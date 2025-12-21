package com.plux.distribution.core.workflow.application.frame.registration.pin;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.frame.utils.WithBuilderFrameFactory;
import com.plux.distribution.core.workflow.application.frame.utils.data.FrameMetadata;
import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CheckPasswordFrame extends AbstractFrame {

    private final String password;

    public CheckPasswordFrame(String password) {
        this.password = password;
    }


    @Override
    public void onEnter(@NotNull FrameContext context) {
        var message = new SimpleMessageContent(
                context.getTextProvider().getString("registration.password.ask_message"),
                List.of()
        );

        context.getManager().send(context, message);

    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        feedback.text().ifPresent(text -> {
            if (text.equals(password)) {
                new InfoMessageFrame(context.getTextProvider().getString("registration.password.correct"))
                        .onEnter(context);
                markFinished();

            } else {
                new InfoMessageFrame(context.getTextProvider().getString("registration.password.incorrect"))
                        .onEnter(context);
            }
        });
    }

    public static class CheckPasswordFrameFactory extends WithBuilderFrameFactory<CheckPasswordFrame>{

        private final String password;

        public CheckPasswordFrameFactory(String password) {
            this.password = password;
        }

        @Override
        public @NotNull CheckPasswordFrame create(@NotNull FrameContext context, @NotNull FrameSnapshot snapshot) {
            return new CheckPasswordFrame(password);
        }

    }
}
