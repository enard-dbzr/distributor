package com.plux.distribution.core.workflow.application.frame.settings.schedule;

import com.plux.distribution.core.interaction.domain.InteractionId;
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

public class AskSpdFrame extends AbstractFrame {

    private final ScheduleSettingsBuilder settingsBuilder;

    private InteractionId lastMessageId = null;

    public AskSpdFrame(ScheduleSettingsBuilder settingsBuilder) {
        this.settingsBuilder = settingsBuilder;
    }

    @Override
    public void onEnter(@NotNull FrameContext context) {
        lastMessageId = context.getManager().send(
                context,
                new SimpleMessageContent(
                        context.getTextProvider().getString("settings.schedule.spd.ask"),
                        List.of()
                )
        );
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        feedback.text().ifPresent(text -> {
            try {
                int count = Integer.parseInt(text);

                settingsBuilder.setSessionsPerDay(count);
                markFinished();

            } catch (IllegalArgumentException e) {
                new InfoMessageFrame(
                        context.getTextProvider().getString("settings.schedule.spd.wrong_format")
                ).onEnter(context);
            }
        });
    }

    public static class AskSpdFrameFactory extends PoolAwareSerializer<AskSpdFrame> {

        @Override
        public PoolNodeSnapshotBuilder buildSnapshot(@NotNull FrameContext context, AskSpdFrame instance,
                PoolNodeSnapshotBuilder builder) {
            return super.buildSnapshot(context, instance, builder)
                    .value("settingsBuilder", context.getObjectPool().put(context, instance.settingsBuilder))
                    .value("lastMessageId", context.getObjectPool().put(context, instance.lastMessageId));
        }

        @Override
        public AskSpdFrame create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            var instance = new AskSpdFrame(
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
