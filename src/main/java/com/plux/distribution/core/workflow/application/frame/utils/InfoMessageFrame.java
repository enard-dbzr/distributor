package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class InfoMessageFrame extends PassThroughFrame {

    private String text = "";
    private @Nullable Frame next = null;

    public InfoMessageFrame(FrameContext context, Frame parent, @Nullable Frame next, String text) {
        super(context, parent);
        this.text = text;
        this.next = next;
    }

    public InfoMessageFrame(@NotNull FrameContext context) {
        super(context);
    }

    @Override
    public void onEnter() {
        context.getManager().send(context, new SimpleMessageContent(text, List.of()));
        changeState(next);
    }

    public static class InfoMessageFrameFactory extends WithBuilderFrameFactory<InfoMessageFrame> {


        @Override
        protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context,
                @NotNull InfoMessageFrame frame, @NotNull FrameSnapshotBuilder builder) {
            return super.buildSnapshot(context, frame, builder)
                    .addData("text", context.getObjectPool().put(context, frame.text))
                    .addData("next", context.getObjectPool().put(context, frame.next));
        }

        @Override
        public @NotNull InfoMessageFrame create(@NotNull FrameContext context) {
            return new InfoMessageFrame(context);
        }

        @Override
        public void restore(@NotNull FrameContext context, @NotNull InfoMessageFrame instance,
                @NotNull FrameSnapshot snapshot) {
            super.restore(context, instance, snapshot);

            instance.text = context.getObjectPool().getData(
                    context,
                    snapshot.data().get("text"),
                    String.class
            );
            instance.next = context.getObjectPool().getData(
                    context,
                    snapshot.data().get("next"),
                    Frame.class
            );
        }
    }
}
