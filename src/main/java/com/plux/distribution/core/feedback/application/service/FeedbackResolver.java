package com.plux.distribution.core.feedback.application.service;

import com.plux.distribution.core.feedback.application.dto.Feedback;
import com.plux.distribution.core.feedback.application.dto.ResolvedFeedback;
import com.plux.distribution.core.feedback.application.port.in.FeedbackProcessor;
import com.plux.distribution.core.feedback.application.port.in.ResolvedFeedbackProcessor;
import com.plux.distribution.core.feedback.domain.payload.ButtonPayload;
import com.plux.distribution.core.feedback.domain.payload.FeedbackPayloadVisitor;
import com.plux.distribution.core.feedback.domain.payload.MessagePayload;
import com.plux.distribution.core.feedback.domain.payload.ReplyPayload;
import com.plux.distribution.core.interaction.application.dto.InteractionDto;
import com.plux.distribution.core.interaction.application.port.in.GetInteractionUseCase;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

public class FeedbackResolver implements FeedbackProcessor {

    private final List<ResolvedFeedbackProcessor> processors;
    private final GetInteractionUseCase getInteractionUseCase;

    public FeedbackResolver(List<ResolvedFeedbackProcessor> processors, GetInteractionUseCase getInteractionUseCase) {
        this.processors = processors;
        this.getInteractionUseCase = getInteractionUseCase;
    }


    @Override
    public void process(@NotNull Feedback feedback) {
        var replyTo = feedback.payload().accept(new FeedbackPayloadVisitor<InteractionId>() {
            @Override
            public InteractionId visit(@NotNull ButtonPayload entity) {
                return entity.replyTo();
            }

            @Override
            public InteractionId visit(@NotNull MessagePayload entity) {
                return null;
            }

            @Override
            public InteractionId visit(@NotNull ReplyPayload entity) {
                return entity.replyTo();
            }
        });

        AtomicReference<InteractionDto> replyMessage = new AtomicReference<>();
        if (replyTo != null) {
            replyMessage.set(getInteractionUseCase.get(replyTo));
        } else {
            getInteractionUseCase.getLastOfRecipient(new ChatParticipant(feedback.chatId()))
                    .ifPresent(replyMessage::set);
        }

        if (replyMessage.get() == null) {
            return;
        }

        var resolvedFeedback = new ResolvedFeedback(feedback, replyMessage.get());

        for (var processor : processors) {
            processor.process(resolvedFeedback);
        }
    }
}
