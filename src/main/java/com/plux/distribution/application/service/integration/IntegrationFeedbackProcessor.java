package com.plux.distribution.application.service.integration;

import com.plux.distribution.application.dto.message.MessageDto;
import com.plux.distribution.application.port.in.FeedbackProcessor;
import com.plux.distribution.application.port.in.integration.NotifyServiceFeedbackPort;
import com.plux.distribution.application.port.in.message.GetMessageUseCase;
import com.plux.distribution.application.service.FeedbackContext;
import com.plux.distribution.domain.feedback.payload.ButtonPayload;
import com.plux.distribution.domain.feedback.payload.FeedbackPayloadVisitor;
import com.plux.distribution.domain.feedback.payload.MessagePayload;
import com.plux.distribution.domain.feedback.payload.ReplyPayload;
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
    public void process(@NotNull FeedbackContext feedback) {
        var replyTo = new AtomicReference<MessageId>();

        feedback.feedback().payload().accept(new FeedbackPayloadVisitor() {
            @Override
            public void visit(@NotNull ButtonPayload entity) {
                replyTo.set(entity.replyTo());
            }

            @Override
            public void visit(@NotNull MessagePayload entity) {
                replyTo.set(null);
            }

            @Override
            public void visit(@NotNull ReplyPayload entity) {
                replyTo.set(entity.replyTo());
            }
        });

        AtomicReference<MessageDto> message = new AtomicReference<>();
        if (replyTo.get() != null) {
            message.set(getMessageUseCase.getMessage(replyTo.get()));
        } else {
            getMessageUseCase.getLastOfRecipient(new ChatParticipant(feedback.feedback().chatId()))
                    .ifPresent(message::set);
        }

        if (message.get() == null) {
            return;
        }

        if (message.get().sender() instanceof ServiceParticipant(ServiceId serviceId)) {
            notifyServiceFeedbackPort.notifyGotFeedback(serviceId, feedback.feedback());
        }
    }
}
