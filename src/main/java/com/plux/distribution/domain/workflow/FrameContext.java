package com.plux.distribution.domain.workflow;

import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.user.UserId;
import java.util.List;
import java.util.Stack;
import org.jetbrains.annotations.NotNull;

public class FrameContext {

    private final @NotNull FrameContextManager manager;

    private final @NotNull FrameFactory factory;
    private final @NotNull UserId userId;

    private final @NotNull FlowData data = new FlowData();
    private final @NotNull Stack<FrameEntry> frames = new Stack<>();

    public FrameContext(@NotNull FrameContextManager manager, @NotNull FrameFactory factory,
            @NotNull UserId userId) {
        this.manager = manager;
        this.factory = factory;
        this.userId = userId;
    }

    public void exec() {
        frames.peek().frame().exec(this);
    }

    public void handle(@NotNull Feedback feedback) {
        frames.pop().frame().handle(this, feedback);

        if (!frames.isEmpty() && frames.peek().execute()) {
            frames.peek().frame().exec(this);
        }
    }

    public void send(@NotNull Frame frame, @NotNull Message message) {
        manager.send(this, frame, message);
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

    public @NotNull UserId getUserId() {
        return userId;
    }

    public @NotNull FlowData getData() {
        return data;
    }

    public record ContextSnapshot(FlowData.FlowDataSnapshot data,
                                  List<FrameEntry.FrameEntrySnapshot> stack) {

    }
}
