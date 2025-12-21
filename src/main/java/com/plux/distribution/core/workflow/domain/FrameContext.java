package com.plux.distribution.core.workflow.domain;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.workflow.application.frame.utils.RootFrame;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class FrameContext {

    private final @NotNull FrameContextManager manager;

    private final @NotNull TextProvider textProvider;

    private final @NotNull FrameRegistry frameRegistry;

    private final @NotNull ChatId chatId;

    private final @NotNull ObjectPool objectPool;

    private RootFrame root;


    public FrameContext(@NotNull FrameContextManager manager, @NotNull TextProvider textProvider,
            @NotNull FrameRegistry frameRegistry,
            @NotNull ChatId chatId, @NotNull ObjectPool objectPool) {
        this.manager = manager;
        this.textProvider = textProvider;
        this.frameRegistry = frameRegistry;
        this.chatId = chatId;
        this.objectPool = objectPool;
    }

    public @NotNull FrameContextManager getManager() {
        return manager;
    }

    public @NotNull TextProvider getTextProvider() {
        return textProvider;
    }

    public @NotNull FrameRegistry getFrameRegistry() {
        return frameRegistry;
    }

    public @NotNull ChatId getChatId() {
        return chatId;
    }

    public @NotNull ObjectPool getObjectPool() {
        return objectPool;
    }

    public RootFrame getRoot() {
        return root;
    }

    public void setRoot(RootFrame root) {
        this.root = root;
    }
}
