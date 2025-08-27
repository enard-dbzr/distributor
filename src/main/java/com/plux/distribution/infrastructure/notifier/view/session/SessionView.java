package com.plux.distribution.infrastructure.notifier.view.session;

import com.plux.distribution.application.dto.session.SessionDto;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SessionView(
        long id,
        long chatId,
        @NotNull String state,
        @NotNull Date openTime,
        @Nullable Date closeTime
) {

    public SessionView(SessionDto dto) {
        this(dto.id().value(), dto.chatId().value(), dto.state().name(), dto.openTime(), dto.closeTime());
    }
}
