package com.plux.distribution.core.feedback.application.service;

import com.plux.distribution.core.feedback.application.port.out.FeedbackProcessor;
import com.plux.distribution.core.feedback.application.port.out.ResolvedFeedbackProcessor;
import com.plux.distribution.core.feedback.domain.Feedback;
import com.plux.distribution.core.feedback.domain.ResolvedFeedback;
import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import com.plux.distribution.core.interaction.application.port.in.GetInteractionUseCase;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.content.ButtonClickContent;
import com.plux.distribution.core.interaction.domain.content.ReplyMessageContent;
import com.plux.distribution.core.interaction.domain.content.SimpleMessageContent;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class FeedbackResolver implements FeedbackProcessor {

    private static final Logger log = LogManager.getLogger(FeedbackResolver.class);
    private final List<ResolvedFeedbackProcessor> processors;
    private final GetInteractionUseCase getInteractionUseCase;

    public FeedbackResolver(List<ResolvedFeedbackProcessor> processors, GetInteractionUseCase getInteractionUseCase) {
        this.processors = processors;
        this.getInteractionUseCase = getInteractionUseCase;
    }


    @Override
    public void process(@NotNull Feedback feedback) {
        var replyTo = switch (feedback.payload().content()) {
            case ButtonClickContent c -> c.source();
            case ReplyMessageContent c -> c.replyTo();
            case SimpleMessageContent _ -> null;
        };

        InteractionDto replyMessage;
        if (replyTo != null) {
            replyMessage = getInteractionUseCase.get(replyTo);
        } else {
            replyMessage = getInteractionUseCase.getLastOfRecipient(new ChatParticipant(feedback.chatId()))
                    .orElse(null);
        }

        if (replyMessage == null) {
            log.warn("Failed to resolve feedback");
            return;
        }

        var resolvedFeedback = new ResolvedFeedback(feedback, replyMessage);

        for (var processor : processors) {
            processor.process(resolvedFeedback);
        }
    }
}
