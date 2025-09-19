package com.plux.distribution.core.session.application.service;

import com.plux.distribution.core.feedback.application.dto.ResolvedFeedback;
import com.plux.distribution.core.feedback.application.port.in.ResolvedFeedbackProcessor;
import com.plux.distribution.core.session.application.port.in.StartSessionUseCase;
import com.plux.distribution.core.message.domain.participant.ServiceParticipant;
import com.plux.distribution.core.integration.domain.ServiceId;
import org.jetbrains.annotations.NotNull;

public class SessionFeedbackProcessor implements ResolvedFeedbackProcessor {

    private final StartSessionUseCase startSessionUseCase;

    public SessionFeedbackProcessor(StartSessionUseCase startSessionUseCase) {
        this.startSessionUseCase = startSessionUseCase;
    }


    @Override
    public void process(@NotNull ResolvedFeedback resolvedFeedback) {
        if (resolvedFeedback.replyTo().sender() instanceof ServiceParticipant(ServiceId serviceId)) {
            startSessionUseCase.start(resolvedFeedback.feedback().chatId(), serviceId);
        }
    }
}
