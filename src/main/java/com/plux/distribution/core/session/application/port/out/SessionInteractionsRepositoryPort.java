package com.plux.distribution.core.session.application.port.out;

import com.plux.distribution.core.session.domain.SessionId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SessionInteractionsRepositoryPort {

    void setLeftInteractionsCount(@NotNull SessionId sessionId, @Nullable Integer count);

    @Nullable Integer getLeftInteractionsCount(@NotNull SessionId sessionId);
}
