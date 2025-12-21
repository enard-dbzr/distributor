package com.plux.distribution.core.workflow.application.frame.registration.hello;

import com.plux.distribution.core.interaction.application.dto.action.ClearButtonsAction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.MessageAttachment.ButtonAttachment;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.frame.utils.PassThroughFrame;
import com.plux.distribution.core.workflow.application.frame.utils.WithBuilderFrameFactory;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class HelloFrame extends PassThroughFrame {

    private InteractionId lastMessageId = null;

    public HelloFrame(FrameContext context, Frame parent) {
        super(context, parent);
    }

    public HelloFrame(@NotNull FrameContext context) {
        super(context);
    }

    @Override
    public void onEnter() {
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
    public void handle(@NotNull FrameFeedback feedback) {
        if (lastMessageId != null) {
            context.getManager().dispatch(context, new ClearButtonsAction(
                    context.getChatId(),
                    lastMessageId
            ));
        }

        changeState(new InfoMessageFrame(
                context, this, null,
                context.getTextProvider().getString("registration.start.confirmed")
        ));
    }

    public static class HelloFrameFactory extends WithBuilderFrameFactory<HelloFrame> {

        @Override
        protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context, @NotNull HelloFrame frame,
                @NotNull FrameSnapshotBuilder builder) {
            return super.buildSnapshot(context, frame, builder)
                    .addData("lastMessageId", context.getObjectPool().put(context, frame.lastMessageId));
        }

        @Override
        public @NotNull HelloFrame create(@NotNull FrameContext context) {
            return new HelloFrame(context);
        }

        @Override
        public void restore(@NotNull FrameContext context, @NotNull HelloFrame instance,
                @NotNull FrameSnapshot snapshot) {
            super.restore(context, instance, snapshot);

            instance.lastMessageId = context.getObjectPool().getData(
                    context,
                    snapshot.data().get("lastMessageId"),
                    InteractionId.class
            );
        }
    }


}
