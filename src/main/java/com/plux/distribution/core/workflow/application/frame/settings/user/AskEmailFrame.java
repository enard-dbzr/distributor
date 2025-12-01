package com.plux.distribution.core.workflow.application.frame.settings.user;

import com.plux.distribution.core.interaction.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.frame.utils.LastMessageData;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskEmailFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        var messageId = context.send(new SimpleMessageContent(
                context.getTextProvider().getString("registration.user.email.ask"),
                List.of(new ButtonAttachment(context.getTextProvider().getString("utils.skip_button"), "skip"))
        ));

        context.getData().put(LastMessageData.class, new LastMessageData(messageId));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        var userBuilder = context.getData().get(UserBuilder.class);

        feedback.buttonTag().ifPresent(value -> {
            if (value.equals("skip")) {
                userBuilder.setEmail(null);
                goNext(context);
            }
        });

        feedback.text().ifPresent(text -> {
            try {
                userBuilder.setEmail(text);
                goNext(context);
            } catch (IllegalArgumentException e) {
                context.changeState(this, false);
                context.push(
                        new InfoMessageFrame(
                                context.getTextProvider().getString("registration.user.email.wrong_type")
                        ),
                        true);
                context.exec();
            }
        });
    }

    private void goNext(@NotNull FrameContext context) {
        if (context.getData().contains(LastMessageData.class)) {
            context.dispatch(
                    new ClearButtonsAction(
                            context.getChatId(),
                            context.getData().get(LastMessageData.class).interactionId()
                    )
            );

            context.getData().remove(LastMessageData.class);
        }

        context.changeState();
    }
}
