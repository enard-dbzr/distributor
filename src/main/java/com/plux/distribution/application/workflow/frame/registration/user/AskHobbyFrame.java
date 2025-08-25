package com.plux.distribution.application.workflow.frame.registration.user;

import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.domain.action.ClearButtonsAction;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskHobbyFrame implements Frame {

    @Override
    public @NotNull String getKey() {
        return "registration.user.ask_hobby";
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var messageId = context.send(new SimpleMessageContent(
                "Расскажи немного о своих интересах ✨",
                List.of(new ButtonAttachment("Пропустить", "skip"))
        ));

        context.getData().put(AskHobbyMessageId.class, new AskHobbyMessageId(messageId));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        var userBuilder = context.getData().get(UserBuilder.class);

        feedback.buttonTag().ifPresent(value -> {
            if (value.equals("skip")) {
                userBuilder.setHobby(null);
                goNext(context);
            }
        });

        feedback.text().ifPresent(text -> {
            userBuilder.setHobby(text);
            goNext(context);
        });
    }

    private void goNext(@NotNull FrameContext context) {
        context.changeState();

        if (context.getData().contains(AskHobbyMessageId.class)) {
            context.dispatch(
                    new ClearButtonsAction(
                            context.getChatId(),
                            context.getData().get(AskHobbyMessageId.class).messageId()
                    )
            );

            context.getData().remove(AskHobbyMessageId.class);
        }
    }

    private record AskHobbyMessageId(MessageId messageId) {

    }
}
