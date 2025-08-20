package com.plux.distribution.application.workflow.frame.registration.user;

import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.domain.action.ClearButtonsAction;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskCityFrame implements Frame {

    @Override
    public @NotNull String getKey() {
        return "registration.user.ask_city";
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var messageId = context.send(new Message(
                new ChatParticipant(context.getChatId()),
                new SimpleMessageContent(
                        "А из какого ты города? 🌍",
                        List.of(new ButtonAttachment("Пропустить", "skip"))
                )
        ));

        context.getData().put(AskCityMessageId.class, new AskCityMessageId(messageId));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        var userBuilder = context.getData().get(UserBuilder.class);

        feedback.buttonTag().ifPresent(value -> {
            if (value.equals("skip")) {
                userBuilder.setCity(null);
                goNext(context);
            }
        });

        feedback.text().ifPresent(text -> {
            userBuilder.setCity(text);
            goNext(context);
        });
    }

    private void goNext(@NotNull FrameContext context) {
        context.changeState();

        if (context.getData().contains(AskCityMessageId.class)) {
            context.dispatch(
                    new ClearButtonsAction(
                            context.getChatId(),
                            context.getData().get(AskCityMessageId.class).messageId()
                    )
            );

            context.getData().remove(AskCityMessageId.class);
        }
    }

    private record AskCityMessageId(MessageId messageId) {

    }
}
