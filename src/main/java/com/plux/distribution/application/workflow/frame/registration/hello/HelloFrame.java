package com.plux.distribution.application.workflow.frame.registration.hello;

import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.domain.action.ClearButtonsAction;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class HelloFrame implements Frame {

    @Override
    public @NotNull String getKey() {
        return "registration.hello_frame";
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var message = new Message(
                new ChatParticipant(context.getChatId()),
                new SimpleMessageContent(
                        "Добро пожаловать в бота компании 3DiVi! Я здесь, "
                                + "чтобы помочь вам оценить и развить ваши профессиональные навыки. "
                                + "Пройдите тесты, получите обратную связь и станьте еще более "
                                + "квалифицированным специалистом. Начнем?\n(✿◠‿◠)",
                        List.of(
                                new ButtonAttachment("Поехали", "start")
                        )
                )
        );

        var messageId = context.send(message);
        context.getData().put(HelloFrameData.class, new HelloFrameData(messageId));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        if (context.getData().contains(HelloFrameData.class)) {
            var data = context.getData().get(HelloFrameData.class);
            context.dispatch(new ClearButtonsAction(
                    context.getChatId(),
                    data.messageId()
            ));
            context.getData().remove(HelloFrameData.class);
        }

        context.changeState(new PostHelloFrame());
    }

    private record HelloFrameData(@NotNull MessageId messageId) {}
}
