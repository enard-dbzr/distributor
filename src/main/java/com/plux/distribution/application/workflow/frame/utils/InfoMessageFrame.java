package com.plux.distribution.application.workflow.frame.utils;

import com.plux.distribution.domain.workflow.Frame;
import com.plux.distribution.domain.workflow.FrameContext;
import com.plux.distribution.domain.workflow.FrameFeedback;
import com.plux.distribution.domain.message.content.SimpleMessageContent;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class InfoMessageFrame implements Frame {

    private final String text;

    public InfoMessageFrame(String text) {
        this.text = text;
    }

    @Override
    public void exec(@NotNull FrameContext context) {
        context.send(new SimpleMessageContent(text, List.of()));

        context.changeState();
    }

    @Override
    public void handle(@NotNull FrameContext context, @NotNull FrameFeedback feedback) {

    }
}
