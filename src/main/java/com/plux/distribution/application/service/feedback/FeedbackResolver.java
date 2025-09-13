package com.plux.distribution.application.service.feedback;

import com.plux.distribution.application.dto.feedback.dto.Feedback;
import com.plux.distribution.application.dto.feedback.dto.ResolvedFeedback;
import com.plux.distribution.application.dto.feedback.dto.payload.ButtonPayload;
import com.plux.distribution.application.dto.feedback.dto.payload.FeedbackPayloadVisitor;
import com.plux.distribution.application.dto.feedback.dto.payload.MessagePayload;
import com.plux.distribution.application.dto.feedback.dto.payload.ReplyPayload;
import com.plux.distribution.application.dto.message.MessageDto;
import com.plux.distribution.application.port.in.feedback.FeedbackProcessor;
import com.plux.distribution.application.port.in.feedback.ResolvedFeedbackProcessor;
import com.plux.distribution.application.port.in.message.GetMessageUseCase;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.participant.ChatParticipant;
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
