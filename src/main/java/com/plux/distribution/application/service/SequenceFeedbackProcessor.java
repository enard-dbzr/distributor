package com.plux.distribution.application.service;

import com.plux.distribution.application.port.in.FeedbackProcessor;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class SequenceFeedbackProcessor implements FeedbackProcessor {
    private final List<FeedbackProcessor> processors;

    public SequenceFeedbackProcessor(List<FeedbackProcessor> processors) {
        this.processors = processors;
    }

    @Override
    public void process(@NotNull FeedbackContext feedback) {
        for (FeedbackProcessor processor : processors) {
            processor.process(feedback);
        }
    }
}
