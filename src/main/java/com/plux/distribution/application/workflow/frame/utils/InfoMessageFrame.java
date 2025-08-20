package com.plux.distribution.application.workflow.frame.utils;

import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFeedback;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class InfoMessageFrame implements Frame {
    private final String text;

    public InfoMessageFrame(String text) {
        this.text = text;
    }

    @Override
    public @NotNull String getKey() {
        return "utils.info";
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        context.send(new Message(
                new ChatParticipant(context.getChatId()),
                new SimpleMessageContent(
                        text,
                        List.of()
                )
        ));

        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
