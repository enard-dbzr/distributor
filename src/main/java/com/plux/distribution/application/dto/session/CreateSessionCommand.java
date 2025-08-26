package com.plux.distribution.application.dto.session;

import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.service.ServiceId;
import com.plux.distribution.domain.session.SessionState;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CreateSessionCommand(
        @NotNull ChatId chatId,
        @NotNull ServiceId serviceId,
        @NotNull SessionState state,
        @NotNull Date openTime,
        @Nullable Date closeTime
) {

}
