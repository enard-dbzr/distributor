package com.plux.distribution.core.session.application.service;

import com.plux.distribution.core.session.application.command.CreateSessionCommand;
import com.plux.distribution.core.session.application.dto.SessionDto;
import com.plux.distribution.core.session.application.port.in.OpenSessionUseCase;
import com.plux.distribution.core.session.application.port.in.StartSessionUseCase;
import com.plux.distribution.core.session.application.port.out.NotifySessionEventPort;
import com.plux.distribution.core.session.application.port.out.SessionRepositoryPort;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.session.domain.Session;
import com.plux.distribution.core.session.domain.SessionState;
import java.util.Date;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class SessionService implements OpenSessionUseCase, StartSessionUseCase {

    private final @NotNull SessionRepositoryPort sessionRepositoryPort;
    private final @NotNull NotifySessionEventPort notifySessionEventPort;

    public SessionService(@NotNull SessionRepositoryPort sessionRepositoryPort,
            @NotNull NotifySessionEventPort notifySessionEventPort) {
        this.sessionRepositoryPort = sessionRepositoryPort;
        this.notifySessionEventPort = notifySessionEventPort;
    }

    @Override
    public void open(@NotNull ChatId chatId, @NotNull ServiceId serviceId) {
        var session = createOrGet(chatId, serviceId);

        notifySessionEventPort.notifyCreated(new SessionDto(session));
    }

    @Override
    public void start(@NotNull ChatId chatId, @NotNull ServiceId serviceId) {
        var session = createOrGet(chatId, serviceId);

        if (session.getState().equals(SessionState.STARTED)) {
            return;
        }

        session.start();

        sessionRepositoryPort.update(session);

        notifySessionEventPort.notifyStarted(new SessionDto(session));
    }

    private @NotNull Session createOrGet(@NotNull ChatId chatId, @NotNull ServiceId serviceId) {
        var aliveSession = sessionRepositoryPort.findWithStates(
                chatId, serviceId, List.of(SessionState.OPEN, SessionState.STARTED)
        );

        if (aliveSession != null) {
            return aliveSession;
        }

        return sessionRepositoryPort.create(new CreateSessionCommand(
                chatId,
                serviceId,
                SessionState.OPEN,
                new Date(),
                null
        ));
    }
}
