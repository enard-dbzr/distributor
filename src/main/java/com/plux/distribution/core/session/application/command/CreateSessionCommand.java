package com.plux.distribution.core.session.application.command;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.session.domain.SessionState;
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
