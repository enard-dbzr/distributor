package com.plux.distribution.core.session.application.service;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.session.application.port.in.GetScheduleSettingsUseCase;
import com.plux.distribution.core.session.application.port.in.SetScheduleSettingsUseCase;
import com.plux.distribution.core.session.application.port.out.ScheduleSettingsRepository;
import com.plux.distribution.core.session.domain.ScheduleSettings;
import com.plux.distribution.core.session.domain.ScheduleSettings.HoursRange;
import org.jetbrains.annotations.NotNull;

public class ScheduleSettingsService implements SetScheduleSettingsUseCase, GetScheduleSettingsUseCase {

    private final ScheduleSettingsRepository repository;

    public ScheduleSettingsService(ScheduleSettingsRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NotNull ScheduleSettings get(@NotNull ChatId chatId) {
        return repository.get(chatId).orElse(new ScheduleSettings(new HoursRange(0, 24)));
    }

    @Override
    public void set(@NotNull ChatId chatId, @NotNull ScheduleSettings settings) {
        repository.set(chatId, settings);
    }
}
