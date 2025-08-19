package com.plux.distribution.application.workflow.core;

import com.plux.distribution.domain.action.ChatAction;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import java.util.List;
import java.util.Stack;
import org.jetbrains.annotations.NotNull;

public class FrameContext {
    private final @NotNull FrameContextManager manager;

    private final @NotNull FrameFactory factory;
    private final @NotNull ChatId chatId;

    private final @NotNull FlowData data = new FlowData();
    private final @NotNull Stack<FrameEntry> frames = new Stack<>();

    public FrameContext(@NotNull FrameContextManager manager, @NotNull FrameFactory factory,
            @NotNull ChatId chatId) {
        this.manager = manager;
        this.factory = factory;
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

    public @NotNull MessageId send(@NotNull Message message, @NotNull Frame frame) {
        return manager.send(this, frame, message);
    }

    public @NotNull MessageId send(@NotNull Message message) {
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

    public void push(@NotNull Frame frame, boolean execute) {
        frames.push(new FrameEntry(frame, execute));
    }

    public @NotNull ContextSnapshot save() {
        return new ContextSnapshot(
                data.save(),
                frames.stream().map(FrameEntry::save).toList()
        );
    }

    public void restore(@NotNull ContextSnapshot snapshot) {
        data.clear();
        data.restore(snapshot.data);

        frames.clear();
        snapshot.stack.stream().map(f -> FrameEntry.restore(f, factory)).forEach(frames::addLast);
    }

    public @NotNull FrameFactory getFactory() {
        return factory;
    }

    public @NotNull ChatId getChatId() {
        return chatId;
    }

    public @NotNull FlowData getData() {
        return data;
    }

    public record ContextSnapshot(FlowData.FlowDataSnapshot data,
                                  List<FrameEntry.FrameEntrySnapshot> stack) {

    }
}
