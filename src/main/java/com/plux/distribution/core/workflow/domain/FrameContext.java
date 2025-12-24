package com.plux.distribution.core.workflow.domain;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.jetbrains.annotations.NotNull;

public class FrameContext {

    private final @NotNull FrameContextManager manager;

    private final @NotNull TextProvider textProvider;

    private final @NotNull ChatId chatId;

    private final @NotNull FlowData data = new FlowData();
    private final @NotNull Stack<FrameEntry> frames = new Stack<>();

    public FrameContext(@NotNull FrameContextManager manager, @NotNull TextProvider textProvider,
            @NotNull ChatId chatId) {
        this.manager = manager;
        this.textProvider = textProvider;
        this.chatId = chatId;
    }

    public void exec() {
        if (!frames.isEmpty() && frames.peek().execute()) {
            frames.peek().frame().exec(this);
        }
    }

    public void handle(@NotNull FrameFeedback feedback) {
        if (frames.isEmpty()) {
            return;
        }

        frames.peek().frame().handle(this, feedback);
    }

    public @NotNull InteractionId send(@NotNull InteractionContent message, @NotNull Frame frame) {
        return manager.send(this, frame, message);
    }

    public @NotNull InteractionId send(@NotNull InteractionContent message) {
        return send(message, frames.peek().frame());
    }

    public void dispatch(@NotNull ChatAction action) {
        manager.dispatch(this, action);
    }

    public void changeState(@NotNull Frame frame, boolean execute) {
        frames.pop();
        frames.push(new FrameEntry(frame, execute));
        exec();
    }

    public void changeState(@NotNull Frame frame) {
        frames.pop();
        frames.push(new FrameEntry(frame, true));
        exec();
    }

    public void changeState() {
        frames.pop();
        exec();
    }

    public void pop() {
        frames.pop();
    }

    public void clear() {
        frames.clear();
    }

    public boolean isEmpty() {
        return frames.isEmpty();
    }

    public void push(@NotNull Frame frame, boolean execute) {
        frames.push(new FrameEntry(frame, execute));
    }

    public @NotNull ContextSnapshot save() {
        return new ContextSnapshot(
                data.save(),
                frames.stream().toList()
        );
    }

    public void restore(@NotNull ContextSnapshot snapshot) {
        data.clear();
        data.restore(snapshot.data);

        frames.clear();
        snapshot.stack.forEach(frames::addLast);
    }

    public @NotNull ChatId getChatId() {
        return chatId;
    }

    public @NotNull FlowData getData() {
        return data;
    }

    public @NotNull TextProvider getTextProvider() {
        return textProvider;
    }

    public record FrameEntry(Frame frame, boolean execute) {

    }

    public record ContextSnapshot(Map<Class<?>, Object> data,
                                  List<FrameEntry> stack) {

    }
}
