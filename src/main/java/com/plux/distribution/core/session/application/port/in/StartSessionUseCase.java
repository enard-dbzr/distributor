package com.plux.distribution.core.session.application.port.in;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.integration.domain.ServiceId;
import org.jetbrains.annotations.NotNull;

public interface StartSessionUseCase {

    void start(@NotNull ChatId chatId, @NotNull ServiceId serviceId);
}
