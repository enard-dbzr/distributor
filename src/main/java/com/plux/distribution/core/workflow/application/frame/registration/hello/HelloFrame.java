package com.plux.distribution.core.workflow.application.frame.registration.hello;

import com.plux.distribution.core.interaction.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.frame.utils.WithBuilderFrameFactory;
import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class HelloFrame extends AbstractFrame {

    private InteractionId lastMessageId = null;

    @Override
    public void onEnter(@NotNull FrameContext context) {
        var message = new SimpleMessageContent(
                context.getTextProvider().getString("registration.start.hello"),
                List.of(new ButtonAttachment(
                        context.getTextProvider().getString("registration.start.confirm_button"),
                        "start"
                ))
        );

        lastMessageId = context.getManager().send(context, message);
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        if (lastMessageId != null) {
            context.getManager().dispatch(context, new ClearButtonsAction(
                    context.getChatId(),
                    lastMessageId
            ));
        }

        new InfoMessageFrame(context.getTextProvider().getString("registration.start.confirmed"));
        markFinished();
    }

    public static class HelloFrameFactory extends WithBuilderFrameFactory<HelloFrame> {

        @Override
        protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context, @NotNull HelloFrame frame,
                @NotNull FrameSnapshotBuilder builder) {
            return super.buildSnapshot(context, frame, builder)
                    .addData("lastMessageId", context.getObjectPool().put(context, frame.lastMessageId));
        }

        @Override
        public @NotNull HelloFrame create(@NotNull FrameContext context, @NotNull FrameSnapshot snapshot) {
            var frame = new HelloFrame();
            frame.lastMessageId = context.getObjectPool().getData(
                    context,
                    snapshot.data().get("lastMessageId"),
                    InteractionId.class
            );
            return frame;
        }
    }


}
