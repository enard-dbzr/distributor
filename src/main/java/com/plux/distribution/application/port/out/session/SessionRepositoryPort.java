package com.plux.distribution.application.port.out.session;

import com.plux.distribution.application.dto.session.CreateSessionCommand;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.service.ServiceId;
import com.plux.distribution.domain.session.Session;
import com.plux.distribution.domain.session.SessionState;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SessionRepositoryPort {

    @NotNull Session create(@NotNull CreateSessionCommand command);

    void update(@NotNull Session session);

    @Nullable Session findWithStates(
            @NotNull ChatId chatId,
            @NotNull ServiceId serviceId,
            Collection<SessionState> sessionStates
    );
}
