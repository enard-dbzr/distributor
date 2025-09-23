package com.plux.distribution.core.workflow.application.frame.registration.user;

import com.plux.distribution.core.chat.application.port.in.GetChatUseCase;
import com.plux.distribution.core.user.application.command.UserCommand;
import com.plux.distribution.core.chat.application.port.in.AssignUserToChatUseCase;
import com.plux.distribution.core.user.application.port.in.CreateUserUseCase;
import com.plux.distribution.core.user.application.port.in.UpdateUserInfoUseCase;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import org.jetbrains.annotations.NotNull;

public class FinalizeFrame implements Frame {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserInfoUseCase updateUserInfoUseCase;
    private final AssignUserToChatUseCase userToChatUseCase;
    private final GetChatUseCase getChatUseCase;

    public FinalizeFrame(
            CreateUserUseCase createUserUseCase, UpdateUserInfoUseCase updateUserInfoUseCase,
            AssignUserToChatUseCase userToChatUseCase, GetChatUseCase getChatUseCase
    ) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserInfoUseCase = updateUserInfoUseCase;
        this.userToChatUseCase = userToChatUseCase;
        this.getChatUseCase = getChatUseCase;
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var userBuilder = context.getData().get(UserBuilder.class);

        var userInfo = userBuilder.buildUserInfo();

        var userId = getChatUseCase.get(context.getChatId()).userId();
        if (userId == null) {
            var user = createUserUseCase.create(new UserCommand(userInfo));
            userToChatUseCase.assignUser(context.getChatId(), user.id());
        } else {
            updateUserInfoUseCase.update(userId, userInfo);
        }

        context.getData().remove(UserBuilder.class);

        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
