package com.plux.distribution.core.workflow.application.frame.settings.schedule;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.frame.utils.LastMessageData;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskSpdFrame implements Frame {

    @Override
    public void exec(@NotNull FrameContext context) {
        var messageId = context.send(new SimpleMessageContent(
                context.getTextProvider().getString("settings.schedule.spd.ask"),
                List.of()
        ));

        context.getData().put(LastMessageData.class, new LastMessageData(messageId));
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        var settingsBuilder = context.getData().get(ScheduleSettingsBuilder.class);

        feedback.text().ifPresent(text -> {
            try {
                int count = Integer.parseInt(text);
                if (count <= 0) {
                    throw new NumberFormatException("must be positive");
                }

                settingsBuilder.setSessionsPerDay(count);
                context.changeState();

            } catch (NumberFormatException e) {
                context.changeState(this, false);
                context.push(
                        new InfoMessageFrame(
                                context.getTextProvider().getString("settings.schedule.spd.wrong_format")
                        ),
                        true
                );
                context.exec();
            }
        });

    }
}
