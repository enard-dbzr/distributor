package com.plux.distribution.core.feedback.application.service;

import com.plux.distribution.core.feedback.application.dto.Feedback;
import com.plux.distribution.core.feedback.application.dto.ResolvedFeedback;
import com.plux.distribution.core.feedback.domain.payload.ButtonPayload;
import com.plux.distribution.core.feedback.domain.payload.FeedbackPayloadVisitor;
import com.plux.distribution.core.feedback.domain.payload.MessagePayload;
import com.plux.distribution.core.feedback.domain.payload.ReplyPayload;
import com.plux.distribution.core.message.application.dto.MessageDto;
import com.plux.distribution.core.feedback.application.port.in.FeedbackProcessor;
import com.plux.distribution.core.feedback.application.port.in.ResolvedFeedbackProcessor;
import com.plux.distribution.core.message.application.port.in.GetMessageUseCase;
import com.plux.distribution.core.message.domain.MessageId;
import com.plux.distribution.core.message.domain.participant.ChatParticipant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

public class FeedbackResolver implements FeedbackProcessor {
    private final List<ResolvedFeedbackProcessor> processors;
    private final GetMessageUseCase getMessageUseCase;

    public FeedbackResolver(List<ResolvedFeedbackProcessor> processors, GetMessageUseCase getMessageUseCase) {
        this.processors = processors;
        this.getMessageUseCase = getMessageUseCase;
    }


    @Override
    public void process(@NotNull Feedback feedback) {
        var replyTo = feedback.payload().accept(new FeedbackPayloadVisitor<MessageId>() {
            @Override
            public MessageId visit(@NotNull ButtonPayload entity) {
                return entity.replyTo();
            }

            @Override
            public MessageId visit(@NotNull MessagePayload entity) {
                return null;
            }

            @Override
            public MessageId visit(@NotNull ReplyPayload entity) {
                return entity.replyTo();
            }
        });

        AtomicReference<MessageDto> replyMessage = new AtomicReference<>();
        if (replyTo != null) {
            replyMessage.set(getMessageUseCase.getMessage(replyTo));
        } else {
            getMessageUseCase.getLastOfRecipient(new ChatParticipant(feedback.chatId()))
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
