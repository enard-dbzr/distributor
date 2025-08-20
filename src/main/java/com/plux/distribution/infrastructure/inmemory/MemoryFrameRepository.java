package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.port.out.workflow.SaveContextPort;
import com.plux.distribution.application.port.out.workflow.LoadContextPort;
import com.plux.distribution.application.workflow.core.FrameContextManager;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.application.workflow.core.FrameContext;
import com.plux.distribution.application.workflow.core.FrameFactory;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public class MemoryFrameRepository implements SaveContextPort, LoadContextPort {

    private final Map<ChatId, FrameContext.ContextSnapshot> states = new ConcurrentHashMap<>();

    @Override
    public void save(@NotNull FrameContext context) {
        states.put(context.getChatId(), context.save());
    }

    @Override
    public @NotNull Optional<FrameContext> load(@NotNull ChatId chatId, @NotNull FrameContextManager manager,
            @NotNull FrameFactory frameFactory) {
        if (!states.containsKey(chatId)) {
            return Optional.empty();
        }

        var context = new FrameContext(manager, frameFactory, chatId);
        context.restore(states.get(chatId));

        return Optional.of(context);
    }
}
