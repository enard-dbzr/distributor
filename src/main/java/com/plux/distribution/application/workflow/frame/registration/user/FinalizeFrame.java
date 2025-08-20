package com.plux.distribution.application.workflow.frame.registration.user;

import com.plux.distribution.application.port.out.user.CreateUserPort;
import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class FinalizeFrame implements Frame {
    private final CreateUserPort createUserPort;

    public FinalizeFrame(CreateUserPort createUserPort) {
        this.createUserPort = createUserPort;
    }

    @Override
    public @NotNull String getKey() {
        return "registration.user.finalize";
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        var userBuilder = context.getData().get(UserBuilder.class);

        createUserPort.create(userBuilder.buildUserInfo());

        context.getData().remove(UserBuilder.class);

        context.send(new Message(
                new ChatParticipant(context.getChatId()),
                new SimpleMessageContent(
                        "Регистрация прошла успешно ✅\nРад тебя видеть здесь 🤗",
                        List.of()
                )
        ));
        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
