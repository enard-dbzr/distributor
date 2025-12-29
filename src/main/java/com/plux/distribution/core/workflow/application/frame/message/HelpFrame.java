package com.plux.distribution.core.workflow.application.frame.message;

import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.AbstractFrame;
import org.jetbrains.annotations.NotNull;

public class HelpFrame extends AbstractFrame {

    @Override
    public void onEnter(@NotNull FrameContext context) {
        new InfoMessageFrame(context.getTextProvider().getString("help"))
                .onEnter(context);

        markFinished();
    }

    public static class HelpFrameFactory extends PoolAwareSerializer<HelpFrame> {

        @Override
        public HelpFrame create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            return new HelpFrame();
        }

        @Override
        public @NotNull HelpFrame create(@NotNull FrameContext context) {
            return new HelpFrame();
        }
    }
}
