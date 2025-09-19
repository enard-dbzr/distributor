package com.plux.distribution.core.workflow.application.frame.registration.user;

import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.application.frame.utils.LastMessageData;
import com.plux.distribution.core.message.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.message.domain.attachment.ButtonAttachment;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskHobbyFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        var messageId = context.send(new SimpleMessageContent(
                "Расскажи немного о своих интересах ✨",
                List.of(new ButtonAttachment("Пропустить", "skip"))
        ));

        context.getData().put(LastMessageData.class, new LastMessageData(messageId));
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
