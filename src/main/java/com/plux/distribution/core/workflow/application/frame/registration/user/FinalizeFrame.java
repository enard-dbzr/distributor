package com.plux.distribution.core.workflow.application.frame.registration.user;

import com.plux.distribution.core.user.application.command.CreateUserCommand;
import com.plux.distribution.core.chat.application.port.in.AssignUserToChatUseCase;
import com.plux.distribution.core.user.application.port.in.CreateUserUseCase;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFeedback;
import org.jetbrains.annotations.NotNull;

public class FinalizeFrame implements Frame {

    private final CreateUserUseCase createUserUseCase;
    private final AssignUserToChatUseCase userToChatUseCase;

    public FinalizeFrame(CreateUserUseCase createUserUseCase, AssignUserToChatUseCase userToChatUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.userToChatUseCase = userToChatUseCase;
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var userBuilder = context.getData().get(UserBuilder.class);

        var user = createUserUseCase.create(new CreateUserCommand(userBuilder.buildUserInfo()));
        userToChatUseCase.assignUser(context.getChatId(), user.id());

        context.getData().remove(UserBuilder.class);

        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
