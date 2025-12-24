package com.plux.distribution.infrastructure.persistence.entity.session;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.session.domain.ScheduleSettings;
import com.plux.distribution.core.session.domain.ScheduleSettings.HoursRange;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "schedule_settings")
public class ScheduleSettingsEntity {

    @SuppressWarnings("unused")
    @Id
    private Long chatId;

    @Column(nullable = false)
    private Integer fromHour;

    @Column(nullable = false)
    private Integer toHour;

    @Column(nullable = false)
    private String timezone;

    @Column(nullable = false)
    private Integer spd;

    private ScheduleSettingsEntity(Long chatId, Integer fromHour, Integer toHour, String timezone, Integer spd) {
        this.chatId = chatId;
        this.fromHour = fromHour;
        this.toHour = toHour;
        this.timezone = timezone;
        this.spd = spd;
    }

    public ScheduleSettingsEntity() {

    }

    public ScheduleSettings toModel() {
        return new ScheduleSettings(new HoursRange(fromHour, toHour), timezone, spd);
    }

    public static ScheduleSettingsEntity of(ChatId chatId, ScheduleSettings settings) {
        return new ScheduleSettingsEntity(
                chatId.value(),
                settings.hoursRange().from(),
                settings.hoursRange().to(),
                settings.timezone(),
                settings.sessionsPerDay()
        );
    }
}
