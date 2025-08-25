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

public class AskAgeFrame implements Frame {

    @Override
    public @NotNull String getKey() {
        return "registration.user.ask_age";
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var messageId = context.send(new SimpleMessageContent(
                "Сколько тебе лет? 😉",
                List.of(new ButtonAttachment("Пропустить", "skip"))
        ));

        context.getData().put(AskAgeMessageId.class, new AskAgeMessageId(messageId));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        var userBuilder = context.getData().get(UserBuilder.class);

        feedback.buttonTag().ifPresent(value -> {
            if (value.equals("skip")) {
                userBuilder.setAge(null);
                goNext(context);
            }
        });

        feedback.text().ifPresent(text -> {
            try {
                userBuilder.setAge(Integer.valueOf(text));
                goNext(context);
            } catch (NumberFormatException e) {
                context.changeState(this, false);
                context.push(new InfoMessageFrame("Введи число пожалуйста 😄"), true);
                context.exec();
            }
        });
    }

    private void goNext(@NotNull FrameContext context) {
        context.changeState();

        if (context.getData().contains(AskAgeMessageId.class)) {
            context.dispatch(
                    new ClearButtonsAction(
                            context.getChatId(),
                            context.getData().get(AskAgeMessageId.class).messageId()
                    )
            );

            context.getData().remove(AskAgeMessageId.class);
        }
    }

    private record AskAgeMessageId(MessageId messageId) {

    }
}
