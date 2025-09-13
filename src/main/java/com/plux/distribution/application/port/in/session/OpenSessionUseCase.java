package com.plux.distribution.application.port.in.session;

import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public interface OpenSessionUseCase {

    void open(@NotNull ChatId chatId, @NotNull ServiceId serviceId);
}
