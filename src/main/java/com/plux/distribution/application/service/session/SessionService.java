package com.plux.distribution.application.service.session;

import com.plux.distribution.application.dto.session.CreateSessionCommand;
import com.plux.distribution.application.dto.session.SessionDto;
import com.plux.distribution.application.port.in.session.CreateSessionUseCase;
import com.plux.distribution.application.port.out.session.NotifySessionEventPort;
import com.plux.distribution.application.port.out.session.SaveSessionPort;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.service.ServiceId;
import com.plux.distribution.domain.session.SessionState;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class SessionService implements CreateSessionUseCase {

    private final @NotNull SaveSessionPort saveSessionPort;
    private final @NotNull NotifySessionEventPort notifySessionEventPort;

    public SessionService(@NotNull SaveSessionPort saveSessionPort,
            @NotNull NotifySessionEventPort notifySessionEventPort) {
        this.saveSessionPort = saveSessionPort;
        this.notifySessionEventPort = notifySessionEventPort;
    }

    @Override
    public void create(@NotNull ChatId chatId, @NotNull ServiceId serviceId) {
        var session = saveSessionPort.create(new CreateSessionCommand(
                chatId,
                serviceId,
                SessionState.OPEN,
                new Date(),
                null
        ));

        notifySessionEventPort.notifyCreated(new SessionDto(session));
    }
}
