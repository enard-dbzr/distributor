package com.plux.distribution.core.workflow.application.frame.settings.user;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
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

    public static class AskNameFrameFactory extends PoolAwareSerializer<AskNameFrame> {

        @Override
        public PoolNodeSnapshotBuilder buildFrameSnapshot(@NotNull FrameContext context, AskNameFrame instance,
                PoolNodeSnapshotBuilder builder) {
            return super.buildFrameSnapshot(context, instance, builder)
                    .value("userBuilder", context.getObjectPool().put(context, instance.userBuilder));
        }

        @Override
        public AskNameFrame create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            return new AskNameFrame(
                    context.getObjectPool().getData(
                            context,
                            snapshot.values().get("userBuilder"),
                            UserBuilder.class
                    )
            );
        }
    }
}
