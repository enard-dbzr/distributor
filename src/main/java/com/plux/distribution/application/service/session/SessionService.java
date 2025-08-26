package com.plux.distribution.application.service.session;

import com.plux.distribution.application.dto.session.CreateSessionCommand;
import com.plux.distribution.application.port.in.session.CreateSessionUseCase;
import com.plux.distribution.application.port.out.session.SaveSessionPort;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.service.ServiceId;
import com.plux.distribution.domain.session.SessionState;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class SessionService implements CreateSessionUseCase {

    private final @NotNull SaveSessionPort saveSessionPort;

    public SessionService(@NotNull SaveSessionPort saveSessionPort) {
        this.saveSessionPort = saveSessionPort;
    }

    @Override
    public void create(@NotNull ChatId chatId, @NotNull ServiceId serviceId) {
        saveSessionPort.create(new CreateSessionCommand(
                chatId,
                serviceId,
                SessionState.OPEN,
                new Date(),
                null
        ));
    }
}
