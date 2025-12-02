package com.plux.distribution.core.feedback.application.service;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.feedback.application.port.out.FeedbackProcessor;
import com.plux.distribution.core.feedback.domain.Feedback;
import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import com.plux.distribution.core.interaction.application.port.out.BotInteractionHandler;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import java.util.Date;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class FeedbackBotHandler implements BotInteractionHandler {

    private final @NotNull List<FeedbackProcessor> feedbackProcessors;

    public FeedbackBotHandler(@NotNull List<FeedbackProcessor> feedbackProcessors) {
        this.feedbackProcessors = feedbackProcessors;
    }


    @Override
    public void handle(InteractionDto interaction) {
        if (!(interaction.sender() instanceof ChatParticipant(ChatId chatId))) {
            throw new UnsupportedOperationException("Not chat sender is not allowed here");
        }

        var feedback = new Feedback(new Date(), chatId, interaction);

        for (var processor : feedbackProcessors) {
            processor.process(feedback);
        }
    }
}
