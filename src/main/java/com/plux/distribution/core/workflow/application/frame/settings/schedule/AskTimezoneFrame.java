package com.plux.distribution.core.workflow.application.frame.settings.schedule;

import com.plux.distribution.core.interaction.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
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
                context.getTextProvider().getString("settings.schedule.timezone.ask"),
                List.of(
                        new ButtonAttachment(
                                context.getTextProvider().getString("settings.schedule.timezone.option.moscow"),
                                "timezone.moscow"
                        ),
                        new ButtonAttachment(
                                context.getTextProvider().getString("settings.schedule.timezone.option.ekaterinburg"),
                                "timezone.ekaterinburg"
                        ),
                        new ButtonAttachment(
                                context.getTextProvider().getString("settings.schedule.timezone.option.krasnoyarsk"),
                                "timezone.krasnoyarsk"
                        )
                )
        ));

        context.getData().put(LastMessageData.class, new LastMessageData(messageId));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        var settingsBuilder = context.getData().get(ScheduleSettingsBuilder.class);

        feedback.buttonTag().ifPresent(value -> {
            switch (value) {
                case "timezone.moscow" -> settingsBuilder.setTimezone("UTC+3");
                case "timezone.ekaterinburg" -> settingsBuilder.setTimezone("UTC+5");
                case "timezone.krasnoyarsk" -> settingsBuilder.setTimezone("UTC+7");
            }
            goNext(context);
        });

        feedback.text().ifPresent(text -> {
            try {
                settingsBuilder.setTimezone(text);
                goNext(context);
            } catch (IllegalArgumentException e) {
                context.changeState(this, false);
                context.push(
                        new InfoMessageFrame(
                                context.getTextProvider().getString("settings.schedule.timezone.wrong_format")
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
                            context.getData().get(LastMessageData.class).interactionId()
                    )
            );

            context.getData().remove(LastMessageData.class);
        }

        context.changeState();
    }
}
