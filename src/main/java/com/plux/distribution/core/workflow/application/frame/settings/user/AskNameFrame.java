package com.plux.distribution.core.workflow.application.frame.settings.user;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.utils.PassThroughFrame;
import com.plux.distribution.core.workflow.application.frame.utils.WithBuilderFrameFactory;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class AskNameFrame extends PassThroughFrame {

    private UserBuilder userBuilder;

    public AskNameFrame(@NotNull FrameContext context) {
        super(context);
    }

    public AskNameFrame(@NotNull FrameContext context, Frame parent, UserBuilder userBuilder) {
        super(context, parent);
        this.userBuilder = userBuilder;
    }

    @Override
    public void onEnter() {
        context.getManager().send(
                context,
                new SimpleMessageContent(
                        context.getTextProvider().getString("registration.user.name.ask"),
                        List.of()
                )
        );
    }

    @Override
    public void handle(@NotNull FrameFeedback feedback) {
        feedback.text().ifPresent(text -> {
            userBuilder.setName(text);
            changeState();
        });
    }

    public static class AskNameFrameFactory extends WithBuilderFrameFactory<AskNameFrame> {

        @Override
        protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context,
                @NotNull AskNameFrame frame, @NotNull FrameSnapshotBuilder builder) {
            return super.buildSnapshot(context, frame, builder)
                    .addData("userBuilder", context.getObjectPool().put(context, userBuilder));
        }

        @Override
        public @NotNull AskNameFrame create(@NotNull FrameContext context) {
            return new AskNameFrame(context);
        }

        @Override
        public void restore(@NotNull FrameContext context, @NotNull AskNameFrame instance,
                @NotNull FrameSnapshot snapshot) {
            super.restore(context, instance, snapshot);

            instance.userBuilder = context.getObjectPool().getData(
                context,
                snapshot.data().get("userBuilder"),
                UserBuilder.class
            );
        }
    }
}
