package com.plux.distribution.application.workflow.frame.registration.user;

import com.plux.distribution.domain.workflow.Frame;
import com.plux.distribution.domain.workflow.FrameContext;
import com.plux.distribution.domain.workflow.FrameFeedback;
import com.plux.distribution.application.workflow.frame.utils.InfoMessageFrame;
import com.plux.distribution.application.workflow.frame.utils.LastMessageData;
import com.plux.distribution.domain.action.ClearButtonsAction;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskEmailFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        var messageId = context.send(new SimpleMessageContent(
                "Оставь, пожалуйста, свою почту ✉️",
                List.of(new ButtonAttachment("Пропустить", "skip"))
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
                context.push(new InfoMessageFrame("Неправильный email 🙁"), true);
                context.exec();
            }
        });
    }

    private void goNext(@NotNull FrameContext context) {
        if (context.getData().contains(LastMessageData.class)) {
            context.dispatch(
                    new ClearButtonsAction(
                            context.getChatId(),
                            context.getData().get(LastMessageData.class).messageId()
                    )
            );

            context.getData().remove(LastMessageData.class);
        }

        context.changeState();
    }
}
