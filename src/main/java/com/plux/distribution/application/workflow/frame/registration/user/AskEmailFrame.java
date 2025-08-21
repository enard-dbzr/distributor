package com.plux.distribution.application.workflow.frame.registration.user;

import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.application.workflow.frame.utils.InfoMessageFrame;
import com.plux.distribution.domain.action.ClearButtonsAction;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskEmailFrame implements Frame {

    @Override
    public @NotNull String getKey() {
        return "registration.user.ask_email";
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var messageId = context.send(new SimpleMessageContent(
                "Оставь, пожалуйста, свою почту ✉️",
                List.of(new ButtonAttachment("Пропустить", "skip"))
        ));

        context.getData().put(AskEmailMessageId.class, new AskEmailMessageId(messageId));
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
        context.changeState();

        if (context.getData().contains(AskEmailMessageId.class)) {
            context.dispatch(
                    new ClearButtonsAction(
                            context.getChatId(),
                            context.getData().get(AskEmailMessageId.class).messageId()
                    )
            );
            context.getData().remove(AskEmailMessageId.class);
        }
    }

    private record AskEmailMessageId(MessageId messageId) {

    }
}
