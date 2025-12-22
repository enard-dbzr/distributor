package com.plux.distribution.core.workflow.domain;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.workflow.application.frame.utils.RootFrame;
import org.jetbrains.annotations.NotNull;

public class FrameContext {

    private final @NotNull FrameContextManager manager;

    private final @NotNull TextProvider textProvider;

    private final @NotNull ChatId chatId;

    private final @NotNull ObjectPool objectPool;

    private RootFrame root;


    public FrameContext(@NotNull FrameContextManager manager, @NotNull TextProvider textProvider,
            @NotNull ChatId chatId, @NotNull ObjectPool objectPool) {
        this.manager = manager;
        this.textProvider = textProvider;
        this.chatId = chatId;
        this.objectPool = objectPool;
    }

    public @NotNull FrameContextManager getManager() {
        return manager;
    }

    public @NotNull TextProvider getTextProvider() {
        return textProvider;
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
