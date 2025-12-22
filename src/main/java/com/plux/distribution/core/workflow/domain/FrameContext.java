package com.plux.distribution.core.workflow.domain;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import java.util.List;
import java.util.Stack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrameContext {

    private final @NotNull FrameContextManager manager;

    private final @NotNull TextProvider textProvider;

    private final @NotNull ObjectPool objectPool;

    private final @NotNull ChatId chatId;

    private final @NotNull Stack<Frame> frames = new Stack<>();

    public FrameContext(@NotNull FrameContextManager manager, @NotNull TextProvider textProvider,
            @NotNull ObjectPool objectPool,
            @NotNull ChatId chatId) {
        this.manager = manager;
        this.textProvider = textProvider;
        this.objectPool = objectPool;
        this.chatId = chatId;
    }

    public void enter() {
        if (!frames.isEmpty()) {
            frames.peek().onEnter();
        }
    }

    public void handle(@NotNull FrameFeedback feedback) {
        if (frames.isEmpty()) {
            return;
        }

        frames.peek().handle(feedback);
    }

    public @NotNull InteractionId send(@NotNull InteractionContent message, @NotNull Frame frame) {
        return manager.send(this, frame, message);
    }

    public @NotNull InteractionId send(@NotNull InteractionContent message) {
        return send(message, frames.peek());
    }

    public void dispatch(@NotNull ChatAction action) {
        manager.dispatch(this, action);
    }

    public void changeState(@Nullable Frame frame) {
        if (!frames.isEmpty()) {
            frames.pop().onExit();
        }

        if (frame != null) {
            frames.push(frame);
        }

        enter();
    }

    public void changeState() {
        changeState(null);
    }

    public void clear() {
        frames.clear();
    }

    public boolean isEmpty() {
        return frames.isEmpty();
    }

    public void push(@NotNull Frame frame) {
        frames.push(frame);
    }

    public @NotNull ContextSnapshot save() {
        return new ContextSnapshot(frames.stream().map(f -> objectPool.put(this, f)).toList());
    }

    public void restore(@NotNull ContextSnapshot snapshot) {
        frames.clear();
        frames.addAll(
                snapshot.stack().stream()
                        .map(poolId -> objectPool.getData(
                                this,
                                poolId,
                                Frame.class
                        ))
                        .toList()
        );
    }

    public @NotNull ChatId getChatId() {
        return chatId;
    }

    public @NotNull TextProvider getTextProvider() {
        return textProvider;
    }

    public record ContextSnapshot(List<PoolId> stack) {}
}
