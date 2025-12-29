package com.plux.distribution.core.workflow.application.frame.settings.schedule.data;

import com.plux.distribution.core.session.domain.ScheduleSettings;
import com.plux.distribution.core.session.domain.ScheduleSettings.HoursRange;
import com.plux.distribution.core.workflow.application.serializer.JsonDataSerializer;

public class ScheduleSettingsBuilder {

    private String timezone;
    private Integer fromHour;
    private Integer toHour;
    private Integer sessionsPerDay;

    public ScheduleSettings buildSettings() {
        return new ScheduleSettings(new HoursRange(fromHour, toHour), timezone, sessionsPerDay);
    }

    @SuppressWarnings("unused")
    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) throws IllegalArgumentException {
        if (timezone != null) {
            ScheduleSettings.checkTimezoneValid(timezone);
        }
        this.timezone = timezone;
    }

    @SuppressWarnings("unused")
    public Integer getFromHour() {
        return fromHour;
    }

    @SuppressWarnings("unused")
    public Integer getToHour() {
        return toHour;
    }

    public void setRange(Integer fromHour, Integer toHour) throws IllegalArgumentException {
        new HoursRange(fromHour, toHour);  // mb throws exception
        this.fromHour = fromHour;
        this.toHour = toHour;
    }

    @SuppressWarnings("unused")
    public Integer getSessionsPerDay() {
        return sessionsPerDay;
    }

    public void setSessionsPerDay(Integer sessionsPerDay) {
        this.sessionsPerDay = sessionsPerDay;
    }

    public static class ScheduleSettingsBuilderSerializer extends JsonDataSerializer<ScheduleSettingsBuilder> {

        public ScheduleSettingsBuilderSerializer() {
            super(ScheduleSettingsBuilder.class);
        }
    }
}
