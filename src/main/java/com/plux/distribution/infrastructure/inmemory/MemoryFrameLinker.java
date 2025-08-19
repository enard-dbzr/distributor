package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.port.out.workflow.ContextLinkerPort;
import com.plux.distribution.application.port.out.workflow.ContextLoaderPort;
import com.plux.distribution.application.workflow.DefaultContextManager;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFactory;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public class MemoryFrameLinker implements ContextLinkerPort, ContextLoaderPort {

    private final Map<ChatId, FrameContext.ContextSnapshot> states = new ConcurrentHashMap<>();
    private DefaultContextManager manager;
    private final FrameFactory frameFactory;

    public MemoryFrameLinker(FrameFactory frameFactory) {
        this.frameFactory = frameFactory;
    }

    @Override
    public void link(@NotNull FrameContext context, @NotNull MessageId messageId) {
        states.put(context.getChatId(), context.save());
    }

    @Override
    public @NotNull Optional<FrameContext> load(@NotNull ChatId chatId) {
        if (!states.containsKey(chatId)) {
            return Optional.empty();
        }

        var context = new FrameContext(manager, frameFactory, chatId);
        context.restore(states.get(chatId));

        return Optional.of(context);
    }

    @Override
    public @NotNull FrameContext init(ChatId chatId) {
        return new FrameContext(manager, frameFactory, chatId);
    }

    public void setManager(DefaultContextManager manager) {
        this.manager = manager;
    }
}
