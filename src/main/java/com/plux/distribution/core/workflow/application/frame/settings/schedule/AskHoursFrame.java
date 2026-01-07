package com.plux.distribution.core.workflow.application.frame.settings.schedule;

import com.plux.distribution.core.interaction.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.data.ScheduleSettingsBuilder;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.AbstractFrame;
import com.plux.distribution.core.workflow.domain.frame.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AskHoursFrame extends AbstractFrame {

    private final ScheduleSettingsBuilder settingsBuilder;

    private InteractionId lastMessageId = null;

    public AskHoursFrame(ScheduleSettingsBuilder settingsBuilder) {
        this.settingsBuilder = settingsBuilder;
    }

    @Override
    public void onEnter(@NotNull FrameContext context) {
        lastMessageId = context.getManager().send(
                context,
                new SimpleMessageContent(
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
                )
        );
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
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

                try {
                    settingsBuilder.setRange(startHour, endHour);

                    goNext(context);
                } catch (IllegalArgumentException e) {
                    new InfoMessageFrame(
                            context.getTextProvider().getString("settings.schedule.hours.wrong_format")
                    ).onEnter(context);
                }
            } else {
                new InfoMessageFrame(
                        context.getTextProvider().getString("settings.schedule.hours.wrong_format")
                ).onEnter(context);
            }
        });
    }

    private void goNext(@NotNull FrameContext context) {
        if (lastMessageId != null) {
            context.getManager().dispatch(
                    context,
                    new ClearButtonsAction(
                            context.getChatId(),
                            lastMessageId
                    )
            );
        }

        markFinished();
    }

    public static class AskHoursFrameFactory extends PoolAwareSerializer<AskHoursFrame> {

        @Override
        public PoolNodeSnapshotBuilder buildSnapshot(@NotNull FrameContext context, AskHoursFrame instance,
                PoolNodeSnapshotBuilder builder) {
            return super.buildSnapshot(context, instance, builder)
                    .value("settingsBuilder", context.getObjectPool().put(context, instance.settingsBuilder))
                    .value("lastMessageId", context.getObjectPool().put(context, instance.lastMessageId));
        }

        @Override
        public AskHoursFrame create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            var instance = new AskHoursFrame(
                    context.getObjectPool().getData(
                            context,
                            snapshot.values().get("settingsBuilder"),
                            ScheduleSettingsBuilder.class
                    )
            );

            instance.lastMessageId = context.getObjectPool().getData(
                    context,
                    snapshot.values().get("lastMessageId"),
                    InteractionId.class
            );

            return instance;
        }
    }
}
