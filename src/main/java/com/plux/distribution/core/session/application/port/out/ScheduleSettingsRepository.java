package com.plux.distribution.core.session.application.port.out;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.session.domain.ScheduleSettings;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface ScheduleSettingsRepository {

    void set(@NotNull ChatId chatId, @NotNull ScheduleSettings settings);

    Optional<ScheduleSettings> get(@NotNull ChatId chatId);
}
