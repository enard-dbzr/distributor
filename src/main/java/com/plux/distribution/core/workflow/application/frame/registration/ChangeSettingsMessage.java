package com.plux.distribution.core.workflow.application.frame.registration;

import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.AbstractFrame;
import org.jetbrains.annotations.NotNull;

public class ChangeSettingsMessage extends AbstractFrame {

    @Override
    public void onEnter(@NotNull FrameContext context) {
        new InfoMessageFrame(context.getTextProvider().getString("settings.schedule.start"))
                .onEnter(context);

        markFinished();
    }

    public static class ChangeSettingsMessageFactory extends PoolAwareSerializer<ChangeSettingsMessage> {

        @Override
        public ChangeSettingsMessage create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            return new ChangeSettingsMessage();
        }

        @Override
        public @NotNull ChangeSettingsMessage create(@NotNull FrameContext context) {
            return new ChangeSettingsMessage();
        }
    }
}
