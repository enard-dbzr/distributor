package com.plux.distribution.core.workflow.application.frame.registration.hello;

import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.application.frame.utils.LastMessageData;
import com.plux.distribution.core.message.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.message.domain.attachment.ButtonAttachment;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class HelloFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        var message = new SimpleMessageContent(
                context.getTextProvider().getString("registration.start.hello"),
                List.of(new ButtonAttachment(
                        context.getTextProvider().getString("registration.start.confirm_button"),
                        "start"
                ))
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
