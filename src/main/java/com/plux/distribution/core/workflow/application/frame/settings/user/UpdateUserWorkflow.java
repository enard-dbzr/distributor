package com.plux.distribution.core.workflow.application.frame.settings.user;

import com.plux.distribution.core.chat.application.port.in.AssignUserToChatUseCase;
import com.plux.distribution.core.chat.application.port.in.GetChatUseCase;
import com.plux.distribution.core.user.application.command.UserCommand;
import com.plux.distribution.core.user.application.port.in.CreateUserUseCase;
import com.plux.distribution.core.user.application.port.in.UpdateUserInfoUseCase;
import com.plux.distribution.core.workflow.application.frame.settings.user.data.UserBuilder;
import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame;
import com.plux.distribution.core.workflow.application.serializer.PoolAwareSerializer;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.Frame;
import com.plux.distribution.core.workflow.domain.frame.FrameFeedback;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class UpdateUserWorkflow implements Frame {

    private static final Logger log = LogManager.getLogger(UpdateUserWorkflow.class);

    private final @NotNull UserBuilder userBuilder;
    private final @NotNull SequenceFrame workflow;

    private final GetChatUseCase getChatUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserInfoUseCase updateUserInfoUseCase;
    private final AssignUserToChatUseCase userToChatUseCase;

    public UpdateUserWorkflow(GetChatUseCase getChatUseCase, CreateUserUseCase createUserUseCase,
            UpdateUserInfoUseCase updateUserInfoUseCase, AssignUserToChatUseCase userToChatUseCase) {
        this.getChatUseCase = getChatUseCase;
        this.createUserUseCase = createUserUseCase;
        this.updateUserInfoUseCase = updateUserInfoUseCase;
        this.userToChatUseCase = userToChatUseCase;

        userBuilder = new UserBuilder();

        workflow = new SequenceFrame(List.of(
                new AskNameFrame(userBuilder),
                new AskEmailFrame(userBuilder),
                new AskAgeFrame(userBuilder),
                new AskCityFrame(userBuilder),
                new AskHobbyFrame(userBuilder)
        ));
    }

    public UpdateUserWorkflow(@NotNull UserBuilder userBuilder, @NotNull SequenceFrame workflow,
            GetChatUseCase getChatUseCase, CreateUserUseCase createUserUseCase,
            UpdateUserInfoUseCase updateUserInfoUseCase, AssignUserToChatUseCase userToChatUseCase) {
        this.userBuilder = userBuilder;
        this.workflow = workflow;

        this.getChatUseCase = getChatUseCase;
        this.createUserUseCase = createUserUseCase;
        this.updateUserInfoUseCase = updateUserInfoUseCase;
        this.userToChatUseCase = userToChatUseCase;
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

        var userInfo = userBuilder.buildUserInfo();

        log.info("Update user info: {}", userInfo);

        var userId = getChatUseCase.get(context.getChatId()).userId();
        if (userId == null) {
            var user = createUserUseCase.create(new UserCommand(userInfo));
            userToChatUseCase.assignUser(context.getChatId(), user.id());
        } else {
            updateUserInfoUseCase.update(userId, userInfo);
        }
    }

    @Override
    public boolean isFinished() {
        return workflow.isFinished();
    }

    public static class UpdateUserWorkflowFactory extends PoolAwareSerializer<UpdateUserWorkflow> {

        private final GetChatUseCase getChatUseCase;
        private final CreateUserUseCase createUserUseCase;
        private final UpdateUserInfoUseCase updateUserInfoUseCase;
        private final AssignUserToChatUseCase userToChatUseCase;

        public UpdateUserWorkflowFactory(GetChatUseCase getChatUseCase, CreateUserUseCase createUserUseCase,
                UpdateUserInfoUseCase updateUserInfoUseCase, AssignUserToChatUseCase userToChatUseCase) {
            this.getChatUseCase = getChatUseCase;
            this.createUserUseCase = createUserUseCase;
            this.updateUserInfoUseCase = updateUserInfoUseCase;
            this.userToChatUseCase = userToChatUseCase;
        }

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
                    context.getObjectPool().getData(context, snapshot.values().get("workflow"), SequenceFrame.class),
                    getChatUseCase, createUserUseCase, updateUserInfoUseCase, userToChatUseCase
            );
        }

        @Override
        public @NotNull UpdateUserWorkflow create(@NotNull FrameContext context) {
            return new UpdateUserWorkflow(getChatUseCase, createUserUseCase, updateUserInfoUseCase, userToChatUseCase);
        }
    }
}
