package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.port.out.workflow.ContextLinkerPort;
import com.plux.distribution.application.port.out.workflow.ContextLoaderPort;
import com.plux.distribution.application.workflow.DefaultContextManager;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public class MemoryFrameLinker implements ContextLinkerPort, ContextLoaderPort {

    private final Map<MessageId, FrameContext.ContextSnapshot> states = new ConcurrentHashMap<>();
    private DefaultContextManager manager;
    private final FrameFactory frameFactory;

    public MemoryFrameLinker(FrameFactory frameFactory) {
        this.frameFactory = frameFactory;
    }

    @Override
    public void link(@NotNull FrameContext context, @NotNull MessageId messageId) {
        states.put(messageId, context.save());
    }

    @Override
    public @NotNull FrameContext load(ChatId chatId, MessageId messageId) {
        var context = new FrameContext(manager, frameFactory, chatId);

        context.restore(states.get(messageId));

        return context;
    }

    @Override
    public @NotNull FrameContext init(ChatId chatId) {
        return new FrameContext(manager, frameFactory, chatId);
    }

    public void setManager(DefaultContextManager manager) {
        this.manager = manager;
    }
}
