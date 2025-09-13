package com.plux.distribution.application.service.integration;

import com.plux.distribution.application.dto.feedback.dto.ResolvedFeedback;
import com.plux.distribution.application.port.in.feedback.ResolvedFeedbackProcessor;
import com.plux.distribution.application.port.in.integration.NotifyServiceFeedbackPort;
import com.plux.distribution.domain.message.participant.ServiceParticipant;
import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public class IntegrationFeedbackProcessor implements ResolvedFeedbackProcessor {

    private final NotifyServiceFeedbackPort notifyServiceFeedbackPort;

    public IntegrationFeedbackProcessor(NotifyServiceFeedbackPort notifyServiceFeedbackPort) {
        this.notifyServiceFeedbackPort = notifyServiceFeedbackPort;
    }

    @Override
    public void process(@NotNull ResolvedFeedback resolvedFeedback) {
        if (resolvedFeedback.replyTo().sender() instanceof ServiceParticipant(ServiceId serviceId)) {
            notifyServiceFeedbackPort.notifyGotFeedback(serviceId, resolvedFeedback);
        }
    }
}
