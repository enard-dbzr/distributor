package com.plux.distribution.core.session.application.port.out;

import com.plux.distribution.core.session.application.command.CreateSessionCommand;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.session.domain.Session;
import com.plux.distribution.core.session.domain.SessionState;
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
