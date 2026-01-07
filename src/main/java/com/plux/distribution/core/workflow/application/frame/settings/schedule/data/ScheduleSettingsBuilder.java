package com.plux.distribution.core.workflow.application.frame.settings.schedule.data;

import com.plux.distribution.core.session.domain.ScheduleSettings;
import com.plux.distribution.core.session.domain.ScheduleSettings.HoursRange;
import com.plux.distribution.core.workflow.application.serializer.JsonDataSerializer;
import java.time.DateTimeException;
import java.time.ZoneId;

public class ScheduleSettingsBuilder {

    private String timezone;
    private Integer fromHour;
    private Integer toHour;
    private Integer sessionsPerDay;

    public ScheduleSettings build() {
        if (timezone == null ||  fromHour == null || toHour == null || sessionsPerDay == null) {
            throw new IllegalStateException("timezone or fromHour or toHour or sessionsPerDay is null");
        }

        return new ScheduleSettings(new HoursRange(fromHour, toHour), ZoneId.of(timezone), sessionsPerDay);
    }

    @SuppressWarnings("unused")
    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) throws IllegalArgumentException {
        if (timezone != null) {
            try {
                //noinspection ResultOfMethodCallIgnored
                ZoneId.of(timezone);
            } catch (DateTimeException e) {
                throw new IllegalArgumentException("Invalid timezone: " + timezone);
            }
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

    public void setSessionsPerDay(Integer sessionsPerDay) throws IllegalArgumentException {
        if (sessionsPerDay != null) {
            ScheduleSettings.checkSessionsPerDateValid(sessionsPerDay);
        }
        this.sessionsPerDay = sessionsPerDay;
    }

    public static class ScheduleSettingsBuilderSerializer extends JsonDataSerializer<ScheduleSettingsBuilder> {

        public ScheduleSettingsBuilderSerializer() {
            super(ScheduleSettingsBuilder.class);
        }
    }
}
