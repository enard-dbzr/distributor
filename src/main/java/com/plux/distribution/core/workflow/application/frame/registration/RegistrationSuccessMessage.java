package com.plux.distribution.core.workflow.application.frame.registration;

import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.AbstractFrame;
import org.jetbrains.annotations.NotNull;

public class RegistrationSuccessMessage extends AbstractFrame {

    @Override
    public void onEnter(@NotNull FrameContext context) {
        new InfoMessageFrame(context.getTextProvider().getString("registration.finish.success"))
                .onEnter(context);

        markFinished();
    }

    public static class RegistrationSuccessMessageFactory extends PoolAwareSerializer<RegistrationSuccessMessage> {

        @Override
        public RegistrationSuccessMessage create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            return new RegistrationSuccessMessage();
        }

        @Override
        public @NotNull RegistrationSuccessMessage create(@NotNull FrameContext context) {
            return new RegistrationSuccessMessage();
        }
    }
}
