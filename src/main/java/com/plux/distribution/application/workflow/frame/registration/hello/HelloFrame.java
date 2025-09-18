package com.plux.distribution.application.workflow.frame.registration.hello;

import com.plux.distribution.domain.workflow.FrameFeedback;
import com.plux.distribution.application.workflow.frame.utils.LastMessageData;
import com.plux.distribution.domain.action.ClearButtonsAction;
import com.plux.distribution.domain.message.attachment.ButtonAttachment;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.workflow.Frame;
import com.plux.distribution.domain.workflow.FrameContext;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class HelloFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        var message = new SimpleMessageContent(
                "Добро пожаловать в бота компании 3DiVi! Я здесь, "
                        + "чтобы помочь вам оценить и развить ваши профессиональные навыки. "
                        + "Пройдите тесты, получите обратную связь и станьте еще более "
                        + "квалифицированным специалистом. Начнем?\n(✿◠‿◠)",
                List.of(new ButtonAttachment("Поехали", "start"))
        );

        var messageId = context.send(message);
        context.getData().put(LastMessageData.class, new LastMessageData(messageId));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        if (context.getData().contains(LastMessageData.class)) {
            var data = context.getData().get(LastMessageData.class);
            context.dispatch(new ClearButtonsAction(
                    context.getChatId(),
                    data.messageId()
            ));
            context.getData().remove(LastMessageData.class);
        }

        context.changeState(new PostHelloFrame());
    }
}
