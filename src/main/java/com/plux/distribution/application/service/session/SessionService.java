package com.plux.distribution.application.service.session;

import com.plux.distribution.application.dto.session.CreateSessionCommand;
import com.plux.distribution.application.dto.session.SessionDto;
import com.plux.distribution.application.port.in.session.OpenSessionUseCase;
import com.plux.distribution.application.port.in.session.StartSessionUseCase;
import com.plux.distribution.application.port.out.session.NotifySessionEventPort;
import com.plux.distribution.application.port.out.session.SessionRepositoryPort;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.service.ServiceId;
import com.plux.distribution.domain.session.Session;
import com.plux.distribution.domain.session.SessionState;
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
