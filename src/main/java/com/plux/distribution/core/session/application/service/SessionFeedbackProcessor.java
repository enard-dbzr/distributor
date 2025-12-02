package com.plux.distribution.core.session.application.service;

import com.plux.distribution.core.feedback.application.port.out.ResolvedFeedbackProcessor;
import com.plux.distribution.core.feedback.domain.ResolvedFeedback;
import com.plux.distribution.core.integration.application.port.in.FindInteractionSourceUseCase;
import com.plux.distribution.core.session.application.dto.SessionDto;
import com.plux.distribution.core.session.application.port.in.CloseSessionStrategy;
import com.plux.distribution.core.session.application.port.in.CloseSessionUseCase;
import com.plux.distribution.core.session.application.port.in.StartSessionUseCase;
import com.plux.distribution.core.session.application.port.out.SessionRepositoryPort;
import com.plux.distribution.core.session.domain.SessionState;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class SessionFeedbackProcessor implements ResolvedFeedbackProcessor {

    private static final Logger log = LogManager.getLogger(SessionFeedbackProcessor.class);

    private final @NotNull StartSessionUseCase startSessionUseCase;
    private final @NotNull CloseSessionUseCase closeSessionUseCase;
    private final @NotNull CloseSessionStrategy closeSessionStrategy;
    private final @NotNull SessionRepositoryPort sessionRepositoryPort;
    private final @NotNull FindInteractionSourceUseCase findInteractionSourceUseCase;

    public SessionFeedbackProcessor(@NotNull StartSessionUseCase startSessionUseCase,
            @NotNull CloseSessionUseCase closeSessionUseCase,
            @NotNull CloseSessionStrategy closeSessionStrategy, @NotNull SessionRepositoryPort sessionRepositoryPort,
            @NotNull FindInteractionSourceUseCase findInteractionSourceUseCase) {
        this.startSessionUseCase = startSessionUseCase;
        this.closeSessionUseCase = closeSessionUseCase;
        this.closeSessionStrategy = closeSessionStrategy;
        this.sessionRepositoryPort = sessionRepositoryPort;
        this.findInteractionSourceUseCase = findInteractionSourceUseCase;
    }

    @Override
    public void process(@NotNull ResolvedFeedback resolvedFeedback) {

        var serviceId = findInteractionSourceUseCase.findServiceId(resolvedFeedback.replyTo().id());
        if (serviceId == null) {
            return;
        }

        startSessionUseCase.start(resolvedFeedback.feedback().chatId(), serviceId);

        var currentSession = sessionRepositoryPort.findWithStates(
                resolvedFeedback.feedback().chatId(),
                serviceId,
                List.of(SessionState.OPEN, SessionState.STARTED)
        );

        if (currentSession == null) {
            log.warn("No open or started session found after processing input");
            return;
        }

        if (closeSessionStrategy.isCloseNeeded(new SessionDto(currentSession), resolvedFeedback.feedback())) {
            closeSessionUseCase.close(currentSession.getId());
        }
    }
}
