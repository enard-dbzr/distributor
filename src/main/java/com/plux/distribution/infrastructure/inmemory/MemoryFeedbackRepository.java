package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.dto.feedback.command.CreateFeedbackCommand;
import com.plux.distribution.application.port.out.feedback.CreateFeedbackPort;
import com.plux.distribution.application.dto.feedback.dto.Feedback;
import com.plux.distribution.domain.feedback.FeedbackId;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class MemoryFeedbackRepository implements CreateFeedbackPort {
    private final List<Feedback> feedbacks = new ArrayList<>();

    @Override
    public @NotNull Feedback create(@NotNull CreateFeedbackCommand command) {
        var feedback = new Feedback(
                new FeedbackId((long) feedbacks.size()),
                command.actionTime(),
                command.chatId(),
                command.payload()
        );
        feedbacks.add(feedback);
        return feedback;
    }
}
