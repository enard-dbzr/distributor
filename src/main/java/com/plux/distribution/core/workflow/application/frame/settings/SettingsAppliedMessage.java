package com.plux.distribution.core.workflow.application.frame.settings;

import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.AbstractFrame;
import org.jetbrains.annotations.NotNull;

public class SettingsAppliedMessage extends AbstractFrame {

    @Override
    public void onEnter(@NotNull FrameContext context) {
        new InfoMessageFrame(context.getTextProvider().getString("settings.success"))
                .onEnter(context);

        markFinished();

    }

    public static class SettingsAppliedMessageFactory extends PoolAwareSerializer<SettingsAppliedMessage> {

        @Override
        public SettingsAppliedMessage create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            return new SettingsAppliedMessage();
        }

        @Override
        public @NotNull SettingsAppliedMessage create(@NotNull FrameContext context) {
            return new SettingsAppliedMessage();
        }
    }

}
