package com.plux.distribution.application.service.integration;

import com.plux.distribution.application.dto.feedback.dto.Feedback;
import com.plux.distribution.application.dto.message.MessageDto;
import com.plux.distribution.application.port.in.FeedbackProcessor;
import com.plux.distribution.application.port.in.integration.NotifyServiceFeedbackPort;
import com.plux.distribution.application.port.in.message.GetMessageUseCase;
import com.plux.distribution.application.dto.feedback.dto.payload.ButtonPayload;
import com.plux.distribution.application.dto.feedback.dto.payload.FeedbackPayloadVisitor;
import com.plux.distribution.application.dto.feedback.dto.payload.MessagePayload;
import com.plux.distribution.application.dto.feedback.dto.payload.ReplyPayload;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import com.plux.distribution.domain.message.participant.ServiceParticipant;
import com.plux.distribution.domain.service.ServiceId;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

public class IntegrationFeedbackProcessor implements FeedbackProcessor {

    private final GetMessageUseCase getMessageUseCase;
    private final NotifyServiceFeedbackPort notifyServiceFeedbackPort;

    public IntegrationFeedbackProcessor(GetMessageUseCase getMessageUseCase,
            NotifyServiceFeedbackPort notifyServiceFeedbackPort) {
        this.getMessageUseCase = getMessageUseCase;
        this.notifyServiceFeedbackPort = notifyServiceFeedbackPort;
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

        AtomicReference<MessageDto> message = new AtomicReference<>();
        if (replyTo != null) {
            message.set(getMessageUseCase.getMessage(replyTo));
        } else {
            getMessageUseCase.getLastOfRecipient(new ChatParticipant(feedback.chatId()))
                    .ifPresent(message::set);
        }

        if (message.get() == null) {
            return;
        }

        if (message.get().sender() instanceof ServiceParticipant(ServiceId serviceId)) {
            notifyServiceFeedbackPort.notifyGotFeedback(serviceId, feedback);
        }
    }
}
