package com.plux.distribution.core.feedback.application.service;

import com.plux.distribution.core.feedback.application.dto.Feedback;
import com.plux.distribution.core.feedback.application.port.in.FeedbackProcessor;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class SequenceFeedbackProcessor implements FeedbackProcessor {

    private final List<FeedbackProcessor> processors;

    public SequenceFeedbackProcessor(List<FeedbackProcessor> processors) {
        this.processors = processors;
    }

    @Override
    public void process(@NotNull Feedback feedback) {
        for (FeedbackProcessor processor : processors) {
            processor.process(feedback);
        }
    }
}
