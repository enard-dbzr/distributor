package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class InfoMessageFrame extends AbstractFrame {

    private final String text;

    public InfoMessageFrame(String text) {
        this.text = text;
    }

    @Override
    public void onEnter(@NotNull FrameContext context) {
        context.getManager().send(context, new SimpleMessageContent(text, List.of()));
        markFinished();
    }

    public static class InfoMessageFrameFactory extends PoolAwareSerializer<InfoMessageFrame> {

        @Override
        public PoolNodeSnapshotBuilder buildFrameSnapshot(@NotNull FrameContext context, InfoMessageFrame instance,
                PoolNodeSnapshotBuilder builder) {
            return super.buildFrameSnapshot(context, instance, builder)
                    .value("text", context.getObjectPool().put(context, instance.text));
        }

        @Override
        public InfoMessageFrame create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            return new InfoMessageFrame(
                    context.getObjectPool().getData(
                            context,
                            snapshot.values().get("text"),
                            String.class
                    )
            );
        }
    }
}
