package com.plux.distribution.core.workflow.application.frame.registration.pin;

import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import com.plux.distribution.core.workflow.application.frame.utils.InfoMessageFrame;
import com.plux.distribution.core.workflow.application.frame.utils.PassThroughFrame;
import com.plux.distribution.core.workflow.application.frame.utils.WithBuilderFrameFactory;
import com.plux.distribution.core.workflow.application.frame.utils.data.FrameMetadata;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CheckPasswordFrame extends PassThroughFrame {

    private final String password;

    private FrameMetadata metadata = new FrameMetadata();

    public CheckPasswordFrame(@NotNull FrameContext context, String password) {
        super(context);
        this.password = password;
    }

    public CheckPasswordFrame(@NotNull FrameContext context, Frame parent, String password) {
        super(context, parent);
        this.password = password;
    }

    @Override
    public void onEnter() {
        metadata.incrementEnterCount();

        if (metadata.getEnterCount() < 2) {
            var message = new SimpleMessageContent(
                    context.getTextProvider().getString("registration.password.ask_message"),
                    List.of()
            );

            context.getManager().send(context, message);
        }
    }

    @Override
    public void handle(@NotNull FrameFeedback feedback) {
        feedback.text().ifPresent(text -> {
            if (text.equals(password)) {
                changeState(new InfoMessageFrame(
                        context, this, null,
                        context.getTextProvider().getString("registration.password.correct")
                ));
            } else {
                changeState(new InfoMessageFrame(
                        context, this, this,
                        context.getTextProvider().getString("registration.password.incorrect")
                ));
            }
        });
    }

    public static class CheckPasswordFrameFactory extends WithBuilderFrameFactory<CheckPasswordFrame>{

        private final String password;

        public CheckPasswordFrameFactory(String password) {
            this.password = password;
        }

        @Override
        protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context,
                @NotNull CheckPasswordFrame frame, @NotNull FrameSnapshotBuilder builder) {
            return super.buildSnapshot(context, frame, builder)
                    .addData("metadata", context.getObjectPool().put(context, frame.metadata));
        }

        @Override
        public @NotNull CheckPasswordFrame create(@NotNull FrameContext context) {
            return new CheckPasswordFrame(context, password);
        }

        @Override
        public void restore(@NotNull FrameContext context, @NotNull CheckPasswordFrame instance,
                @NotNull FrameSnapshot snapshot) {
            super.restore(context, instance, snapshot);

            instance.metadata = context.getObjectPool().getData(
                    context,
                    snapshot.data().get("metadata"),
                    FrameMetadata.class
            );
        }
    }
}
