package com.plux.distribution.application.service.session;

import com.plux.distribution.application.dto.feedback.dto.ResolvedFeedback;
import com.plux.distribution.application.port.in.feedback.ResolvedFeedbackProcessor;
import com.plux.distribution.application.port.in.session.StartSessionUseCase;
import com.plux.distribution.domain.message.participant.ServiceParticipant;
import com.plux.distribution.domain.service.ServiceId;
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
