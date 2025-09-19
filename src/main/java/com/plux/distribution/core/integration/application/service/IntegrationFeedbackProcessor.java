package com.plux.distribution.core.integration.application.service;

import com.plux.distribution.core.feedback.application.dto.ResolvedFeedback;
import com.plux.distribution.core.feedback.application.port.in.ResolvedFeedbackProcessor;
import com.plux.distribution.core.integration.application.port.in.NotifyServiceFeedbackPort;
import com.plux.distribution.core.message.domain.participant.ServiceParticipant;
import com.plux.distribution.core.integration.domain.ServiceId;
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
