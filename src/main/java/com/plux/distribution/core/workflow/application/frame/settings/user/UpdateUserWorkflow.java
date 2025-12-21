package com.plux.distribution.core.workflow.application.frame.settings.user;

import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame;
import com.plux.distribution.core.workflow.application.frame.utils.WithBuilderFrameFactory;
import com.plux.distribution.core.workflow.domain.AbstractFrame;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import com.plux.distribution.core.workflow.domain.FrameSnapshotBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class UpdateUserWorkflow implements Frame {

    private final @NotNull UserBuilder userBuilder;
    private final @NotNull SequenceFrame workflow;

    public UpdateUserWorkflow() {
        userBuilder = new UserBuilder();

        workflow = new SequenceFrame(List.of(
           new AskNameFrame(userBuilder)
        ));
    }

    public UpdateUserWorkflow(@NotNull UserBuilder userBuilder, @NotNull SequenceFrame workflow) {
        this.userBuilder = userBuilder;
        this.workflow = workflow;
    }

    @Override
    public void onEnter(@NotNull FrameContext context) {
        workflow.onEnter(context);
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {
        workflow.handle(context, feedback);
    }

    @Override
    public void onExit(@NotNull FrameContext context) {
        workflow.onExit(context);
    }

    @Override
    public boolean isFinished() {
        return workflow.isFinished();
    }

    public static class UpdateUserWorkflowFactory extends WithBuilderFrameFactory<UpdateUserWorkflow> {

        @Override
        protected @NotNull FrameSnapshotBuilder buildSnapshot(@NotNull FrameContext context,
                @NotNull UpdateUserWorkflow frame, @NotNull FrameSnapshotBuilder builder) {
            return super.buildSnapshot(context, frame, builder)
                    .addData("user_builder", context.getObjectPool().put(context, frame.userBuilder))
                    .addData("workflow", context.getObjectPool().put(context, frame.workflow));
        }

        @Override
        public @NotNull UpdateUserWorkflow create(@NotNull FrameContext context, @NotNull FrameSnapshot snapshot) {
            return new UpdateUserWorkflow(
                    context.getObjectPool().getData(context, snapshot.data().get("user_builder"), UserBuilder.class),
                    context.getObjectPool().getData(context, snapshot.data().get("workflow"), SequenceFrame.class)
            );
        }
    }
}
