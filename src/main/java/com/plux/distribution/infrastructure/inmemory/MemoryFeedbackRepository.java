package com.plux.distribution.infrastructure.inmemory;

import com.plux.distribution.application.port.out.feedback.CreateFeedbackPort;
import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.feedback.FeedbackId;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class MemoryFeedbackRepository implements CreateFeedbackPort {
    private final List<Feedback> feedbacks = new ArrayList<>();

    @Override
    public @NotNull FeedbackId create(@NotNull Feedback feedback) {
        System.out.println("Put feedback: " + feedback);
        feedbacks.add(feedback);
        return new FeedbackId((long) (feedbacks.size() - 1));
    }
}
