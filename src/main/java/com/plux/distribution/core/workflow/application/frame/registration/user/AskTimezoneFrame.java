package com.plux.distribution.core.workflow.application.frame.registration.user;

import com.plux.distribution.core.message.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.message.domain.attachment.ButtonAttachment;
import com.plux.distribution.core.message.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.frame.utils.LastMessageData;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskTimezoneFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        var messageId = context.send(new SimpleMessageContent(
                context.getTextProvider().getString("registration.user.timezone.ask"),
                List.of(
                        new ButtonAttachment(
                                context.getTextProvider().getString("registration.user.timezone.option.moscow"),
                                "timezone.moscow"
                        ),
                        new ButtonAttachment(
                                context.getTextProvider().getString("registration.user.timezone.option.ekaterinburg"),
                                "timezone.ekaterinburg"
                        ),
                        new ButtonAttachment(
                                context.getTextProvider().getString("registration.user.timezone.option.krasnoyarsk"),
                                "timezone.krasnoyarsk"
                        )
                )
        ));

        context.getData().put(LastMessageData.class, new LastMessageData(messageId));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        var userBuilder = context.getData().get(UserBuilder.class);

        feedback.buttonTag().ifPresent(value -> {
            switch (value) {
                case "timezone.moscow" -> userBuilder.setTimezone("UTC+3");
                case "timezone.ekaterinburg" -> userBuilder.setTimezone("UTC+5");
                case "timezone.krasnoyarsk" -> userBuilder.setTimezone("UTC+7");
            }
            goNext(context);
        });

        feedback.text().ifPresent(text -> {
            try {
                userBuilder.setTimezone(text);
                goNext(context);
            } catch (IllegalArgumentException e) {
                context.changeState(this, false);
                context.push(
                        new InfoMessageFrame(
                                context.getTextProvider().getString("registration.user.timezone.wrong_format")
                        ),
                        true
                );
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
