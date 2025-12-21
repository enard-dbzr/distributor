package com.plux.distribution.core.workflow.application.frame.settings.user;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.utils.WithBuilderFrameFactory;
import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class AskNameFrame extends AbstractFrame {

    private final UserBuilder userBuilder;

    public AskNameFrame(UserBuilder userBuilder) {
        this.userBuilder = userBuilder;
    }

    @Override
    public void onEnter(@NotNull FrameContext context) {
        context.getManager().send(
                context,
                new SimpleMessageContent(
                        context.getTextProvider().getString("registration.user.name.ask"),
                        List.of()
                )
        );
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        feedback.text().ifPresent(text -> {
            userBuilder.setName(text);

            markFinished();
        });
    }

    public static class AskNameFrameFactory extends WithBuilderFrameFactory<AskNameFrame> {

        @Override
        protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context,
                @NotNull AskNameFrame frame, @NotNull FrameSnapshotBuilder builder) {
            return super.buildSnapshot(context, frame, builder)
                    .addData("userBuilder", context.getObjectPool().put(context, frame.userBuilder));
        }

        @Override
        public @NotNull AskNameFrame create(@NotNull FrameContext context, @NotNull FrameSnapshot snapshot) {
            return new AskNameFrame(
                    context.getObjectPool().getData(
                            context,
                            snapshot.data().get("userBuilder"),
                            UserBuilder.class
                    )
            );
        }
    }
}
