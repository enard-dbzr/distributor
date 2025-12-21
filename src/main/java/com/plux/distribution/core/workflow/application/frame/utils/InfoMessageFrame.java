package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class InfoMessageFrame extends AbstractFrame {

    private String text;

    public InfoMessageFrame(String text) {
        this.text = text;
    }

    @Override
    public void onEnter(@NotNull FrameContext context) {
        context.getManager().send(context, new SimpleMessageContent(text, List.of()));
        markFinished();
    }

    public static class InfoMessageFrameFactory extends WithBuilderFrameFactory<InfoMessageFrame> {

        @Override
        protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context,
                @NotNull InfoMessageFrame frame, @NotNull FrameSnapshotBuilder builder) {
            return super.buildSnapshot(context, frame, builder)
                    .addData("text", context.getObjectPool().put(context, frame.text));
        }

        @Override
        public @NotNull InfoMessageFrame create(@NotNull FrameContext context, @NotNull FrameSnapshot snapshot) {
            return new InfoMessageFrame(
                    context.getObjectPool().getData(
                            context,
                            snapshot.data().get("text"),
                            String.class
                    )
            );
        }
    }
}
