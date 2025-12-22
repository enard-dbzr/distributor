package com.plux.distribution.core.workflow.application.frame.settings.user;

import com.plux.distribution.core.workflow.application.frame.settings.user.data.UserBuilder;
import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.frame.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.FrameFeedback;
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

    public static class UpdateUserWorkflowFactory extends PoolAwareSerializer<UpdateUserWorkflow> {

        @Override
        public PoolNodeSnapshotBuilder buildSnapshot(@NotNull FrameContext context, UpdateUserWorkflow instance,
                PoolNodeSnapshotBuilder builder) {
            return super.buildSnapshot(context, instance, builder)
                    .value("user_builder", context.getObjectPool().put(context, instance.userBuilder))
                    .value("workflow", context.getObjectPool().put(context, instance.workflow));
        }

        @Override
        public UpdateUserWorkflow create(@NotNull FrameContext context, PoolNodeSnapshot snapshot) {
            return new UpdateUserWorkflow(
                    context.getObjectPool().getData(context, snapshot.values().get("user_builder"), UserBuilder.class),
                    context.getObjectPool().getData(context, snapshot.values().get("workflow"), SequenceFrame.class)
            );
        }
    }
}
