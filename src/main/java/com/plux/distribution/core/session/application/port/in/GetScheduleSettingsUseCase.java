package com.plux.distribution.core.session.application.port.in;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.session.domain.ScheduleSettings;
import org.jetbrains.annotations.NotNull;

public interface GetScheduleSettingsUseCase {

    @NotNull ScheduleSettings get(@NotNull ChatId chatId);
}
