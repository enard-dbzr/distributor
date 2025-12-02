package com.plux.distribution.core.integration.application.service;

import com.plux.distribution.core.feedback.application.port.out.ResolvedFeedbackProcessor;
import com.plux.distribution.core.feedback.domain.ResolvedFeedback;
import com.plux.distribution.core.integration.application.port.out.NotifyServiceFeedbackPort;
import com.plux.distribution.core.integration.application.port.out.ServiceSendingRepositoryPort;
import com.plux.distribution.core.session.application.port.in.GetCurrentSessionUseCase;
import org.jetbrains.annotations.NotNull;

public class IntegrationFeedbackProcessor implements ResolvedFeedbackProcessor {

    private final @NotNull NotifyServiceFeedbackPort notifyServiceFeedbackPort;
    private final @NotNull GetCurrentSessionUseCase getCurrentSessionUseCase;
    private final @NotNull ServiceSendingRepositoryPort serviceSendingRepositoryPort;

    public IntegrationFeedbackProcessor(@NotNull NotifyServiceFeedbackPort notifyServiceFeedbackPort,
            @NotNull GetCurrentSessionUseCase getCurrentSessionUseCase,
            @NotNull ServiceSendingRepositoryPort serviceSendingRepositoryPort) {
        this.notifyServiceFeedbackPort = notifyServiceFeedbackPort;
        this.getCurrentSessionUseCase = getCurrentSessionUseCase;
        this.serviceSendingRepositoryPort = serviceSendingRepositoryPort;
    }

    @Override
    public void process(@NotNull ResolvedFeedback resolvedFeedback) {
        var serviceId = serviceSendingRepositoryPort.getBySending(resolvedFeedback.replyTo().id());

        var session = getCurrentSessionUseCase.get(resolvedFeedback.feedback().chatId(), serviceId);

        notifyServiceFeedbackPort.notifyGotFeedback(serviceId, resolvedFeedback, session.orElse(null));
    }
}
