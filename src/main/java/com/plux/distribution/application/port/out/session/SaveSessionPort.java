package com.plux.distribution.application.port.out.session;

import com.plux.distribution.application.dto.session.CreateSessionCommand;
import com.plux.distribution.domain.session.Session;
import org.jetbrains.annotations.NotNull;

public interface SaveSessionPort {
    @NotNull Session create(@NotNull CreateSessionCommand command);
    void update(@NotNull Session session);
}
