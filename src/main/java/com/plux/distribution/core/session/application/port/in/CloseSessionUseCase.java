package com.plux.distribution.core.session.application.port.in;

import com.plux.distribution.core.session.domain.SessionId;
import org.jetbrains.annotations.NotNull;

public interface CloseSessionUseCase {
    void close(@NotNull SessionId sessionId);
}
