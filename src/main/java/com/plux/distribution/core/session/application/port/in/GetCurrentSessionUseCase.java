package com.plux.distribution.core.session.application.port.in;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.session.application.dto.SessionDto;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface GetCurrentSessionUseCase {
    Optional<SessionDto> get(@NotNull ChatId chatId, @NotNull ServiceId serviceId);
}
