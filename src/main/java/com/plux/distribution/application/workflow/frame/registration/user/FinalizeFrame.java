package com.plux.distribution.application.workflow.frame.registration.user;

import com.plux.distribution.application.dto.user.CreateUserCommand;
import com.plux.distribution.application.port.in.chat.AssignUserToChatUseCase;
import com.plux.distribution.application.port.in.user.CreateUserUseCase;
import com.plux.distribution.domain.workflow.Frame;
import com.plux.distribution.domain.workflow.FrameContext;
import com.plux.distribution.domain.workflow.FrameFeedback;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import java.util.List;
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

        context.send(new SimpleMessageContent(
                "Регистрация прошла успешно ✅\nРад тебя видеть здесь 🤗",
                List.of()
        ));
        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
