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

public class AskHoursFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        var messageId = context.send(new SimpleMessageContent(
                context.getTextProvider().getString("settings.schedule.hours.ask"),
                List.of(
                        new ButtonAttachment(
                                context.getTextProvider().getString("settings.schedule.hours.option.all_day"),
                                "hours.all_day"
                        ),
                        new ButtonAttachment(
                                context.getTextProvider().getString("settings.schedule.hours.option.extended"),
                                "hours.extended"
                        ),
                        new ButtonAttachment(
                                context.getTextProvider().getString("settings.schedule.hours.option.work_day"),
                                "hours.work_day"
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
                case "hours.all_day" -> settingsBuilder.setRange(0, 24);
                case "hours.extended" -> settingsBuilder.setRange(9, 23);
                case "hours.work_day" -> settingsBuilder.setRange(9, 18);
            }
            goNext(context);
        });

        feedback.text().ifPresent(text -> {
            if (text.matches("^([01]?\\d|2[0-3])-([01]?\\d|2[0-4])$")) {
                String[] parts = text.split("-");
                int startHour = Integer.parseInt(parts[0]);
                int endHour = Integer.parseInt(parts[1]);

                settingsBuilder.setRange(startHour, endHour);

                goNext(context);
            } else {
                context.changeState(this, false);
                context.push(
                        new InfoMessageFrame(
                                context.getTextProvider().getString("settings.schedule.hours.wrong_format")
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
