package com.plux.distribution.core.integration.application.service;

import com.plux.distribution.core.feedback.application.dto.ResolvedFeedback;
import com.plux.distribution.core.feedback.application.port.in.ResolvedFeedbackProcessor;
import com.plux.distribution.core.integration.application.port.in.NotifyServiceFeedbackPort;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.interaction.domain.Participant.ServiceParticipant;
import com.plux.distribution.core.session.application.port.in.GetCurrentSessionUseCase;
import org.jetbrains.annotations.NotNull;

public class IntegrationFeedbackProcessor implements ResolvedFeedbackProcessor {

    private final @NotNull NotifyServiceFeedbackPort notifyServiceFeedbackPort;
    private final @NotNull GetCurrentSessionUseCase getCurrentSessionUseCase;

    public IntegrationFeedbackProcessor(@NotNull NotifyServiceFeedbackPort notifyServiceFeedbackPort,
            @NotNull GetCurrentSessionUseCase getCurrentSessionUseCase) {
        this.notifyServiceFeedbackPort = notifyServiceFeedbackPort;
        this.getCurrentSessionUseCase = getCurrentSessionUseCase;
    }

    @Override
    public void process(@NotNull ResolvedFeedback resolvedFeedback) {
        if (resolvedFeedback.replyTo().sender() instanceof ServiceParticipant(ServiceId serviceId)) {
            var session = getCurrentSessionUseCase.get(resolvedFeedback.feedback().chatId(), serviceId);

            notifyServiceFeedbackPort.notifyGotFeedback(serviceId, resolvedFeedback, session.orElse(null));
        }
    }
}
