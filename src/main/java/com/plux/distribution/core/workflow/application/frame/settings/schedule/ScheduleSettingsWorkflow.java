package com.plux.distribution.core.workflow.application.frame.settings.schedule;

import com.plux.distribution.core.session.application.port.in.SetScheduleSettingsUseCase;
import com.plux.distribution.core.workflow.application.frame.settings.schedule.data.ScheduleSettingsBuilder;
import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.Frame;
import com.plux.distribution.core.workflow.domain.frame.FrameFeedback;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ScheduleSettingsWorkflow implements Frame {

    private final ScheduleSettingsBuilder settingsBuilder;
    private final SequenceFrame workflow;

    private final SetScheduleSettingsUseCase setScheduleSettingsUseCase;

    public ScheduleSettingsWorkflow(SetScheduleSettingsUseCase setScheduleSettingsUseCase) {
        this.settingsBuilder = new ScheduleSettingsBuilder();
        this.workflow = new SequenceFrame(List.of(
                new AskTimezoneFrame(settingsBuilder),
                new AskHoursFrame(settingsBuilder),
                new AskSpdFrame(settingsBuilder)
        ));

        this.setScheduleSettingsUseCase = setScheduleSettingsUseCase;
    }

    public ScheduleSettingsWorkflow(ScheduleSettingsBuilder settingsBuilder, SequenceFrame workflow,
            SetScheduleSettingsUseCase setScheduleSettingsUseCase) {
        this.settingsBuilder = settingsBuilder;
        this.workflow = workflow;

        this.setScheduleSettingsUseCase = setScheduleSettingsUseCase;
    }

    @Override
    public void onEnter(@NotNull FrameContext context) {
        workflow.onEnter(context);
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        workflow.handle(context, feedback);
    }

    @Override
    public void onExit(@NotNull FrameContext context) {
        workflow.onExit(context);

        setScheduleSettingsUseCase.set(context.getChatId(), settingsBuilder.build());
    }

    @Override
    public boolean isFinished() {
        return workflow.isFinished();
    }

    public static class ScheduleSettingsWorkflowFactory extends PoolAwareSerializer<ScheduleSettingsWorkflow> {

        private final SetScheduleSettingsUseCase setScheduleSettingsUseCase;

        public ScheduleSettingsWorkflowFactory(SetScheduleSettingsUseCase setScheduleSettingsUseCase) {
            this.setScheduleSettingsUseCase = setScheduleSettingsUseCase;
        }

        @Override
        public PoolNodeSnapshotBuilder buildSnapshot(@NotNull FrameContext context, ScheduleSettingsWorkflow instance,
                PoolNodeSnapshotBuilder builder) {
            return super.buildSnapshot(context, instance, builder)
                    .value("settings_builder", context.getObjectPool().put(context, instance.settingsBuilder))
                    .value("workflow", context.getObjectPool().put(context, instance.workflow));
        }

        @Override
        public ScheduleSettingsWorkflow create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            return new ScheduleSettingsWorkflow(
                    context.getObjectPool()
                            .getData(context, snapshot.values().get("settings_builder"), ScheduleSettingsBuilder.class),
                    context.getObjectPool().getData(context, snapshot.values().get("workflow"), SequenceFrame.class),
                    setScheduleSettingsUseCase
            );
        }

        @Override
        public @NotNull ScheduleSettingsWorkflow create(@NotNull FrameContext context) {
            return new ScheduleSettingsWorkflow(setScheduleSettingsUseCase);
        }
    }
}
