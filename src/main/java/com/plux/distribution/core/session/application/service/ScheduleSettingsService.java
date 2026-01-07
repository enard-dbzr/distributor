package com.plux.distribution.core.session.application.service;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.session.application.port.in.GetScheduleSettingsUseCase;
import com.plux.distribution.core.session.application.port.in.ScheduleSettingsChangedHandler;
import com.plux.distribution.core.session.application.port.in.SetScheduleSettingsUseCase;
import com.plux.distribution.core.session.application.port.out.ScheduleSettingsRepository;
import com.plux.distribution.core.session.domain.ScheduleSettings;
import com.plux.distribution.core.session.domain.ScheduleSettings.HoursRange;
import java.time.ZoneId;
import org.jetbrains.annotations.NotNull;

public class ScheduleSettingsService implements SetScheduleSettingsUseCase, GetScheduleSettingsUseCase {

    private final ScheduleSettingsRepository repository;

    private ScheduleSettingsChangedHandler handler;

    public ScheduleSettingsService(ScheduleSettingsRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull ScheduleSettings get(@NotNull ChatId chatId) {
        return repository.get(chatId).orElse(
                new ScheduleSettings(new HoursRange(0, 24), ZoneId.of("UTC+5"), 3)
        );
    }

    @Override
    public void set(@NotNull ChatId chatId, @NotNull ScheduleSettings settings) {
        repository.set(chatId, settings);

        handler.onSettingsChanged(chatId, settings);
    }

    public void setHandler(ScheduleSettingsChangedHandler handler) {
        this.handler = handler;
    }
}
