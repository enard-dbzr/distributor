package com.plux.distribution.core.session.application.service;

import com.plux.distribution.core.chat.application.port.in.GetAllChatsUseCase;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.session.application.port.in.CheckChatIsBusyUseCase;
import com.plux.distribution.core.session.application.port.in.GetScheduleSettingsUseCase;
import com.plux.distribution.core.session.application.port.in.InitSessionsStrategy;
import com.plux.distribution.core.session.application.port.in.OpenSessionUseCase;
import com.plux.distribution.core.session.application.port.in.ScheduleSettingsChangedHandler;
import com.plux.distribution.core.session.domain.ScheduleSettings;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class RandomSessionInitializer implements InitSessionsStrategy, ScheduleSettingsChangedHandler {

    private static final Logger log = LogManager.getLogger(RandomSessionInitializer.class);

    private final @NotNull OpenSessionUseCase openSessionUseCase;
    private final @NotNull GetAllChatsUseCase getAllChatsUseCase;
    private final @NotNull CheckChatIsBusyUseCase checkChatIsBusyUseCase;
    private final @NotNull GetScheduleSettingsUseCase getScheduleSettingsUseCase;
    private final @NotNull ServiceId serviceId;  // TODO: Remove this mock

    private final Map<ChatId, ChatSchedule> chatSchedules = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public RandomSessionInitializer(@NotNull OpenSessionUseCase openSessionUseCase,
            @NotNull GetAllChatsUseCase getAllChatsUseCase, @NotNull CheckChatIsBusyUseCase checkChatIsBusyUseCase,
            @NotNull GetScheduleSettingsUseCase getScheduleSettingsUseCase,
            @NotNull ServiceId serviceId) {
        this.openSessionUseCase = openSessionUseCase;
        this.getAllChatsUseCase = getAllChatsUseCase;
        this.checkChatIsBusyUseCase = checkChatIsBusyUseCase;
        this.getScheduleSettingsUseCase = getScheduleSettingsUseCase;
        this.serviceId = serviceId;
    }

    @Override
    public void initSessions() {
        generateScheduleIfNeeded();
        checkAndOpenSessions(ZonedDateTime.now());
        cleanupOldSchedules();
    }

    @Override
    public void onSettingsChanged(ChatId chatId, ScheduleSettings settings) {
        var zonedNow = ZonedDateTime.now(ZoneId.of(settings.timezone()));
        var today = zonedNow.toLocalDate();

        List<ZonedDateTime> newSchedule = generateDailySchedule(today, settings);
        chatSchedules.put(chatId, new ChatSchedule(zonedNow, newSchedule));
        log.info("Regenerated schedule for {}: {}", chatId, newSchedule);
    }

    private void generateScheduleIfNeeded() {
        var chats = getAllChatsUseCase.getAllChatIds();

        for (var chatId : chats) {
            try {
                var schedule = chatSchedules.get(chatId);
                var settings = getScheduleSettingsUseCase.get(chatId);

                var zoneId = ZoneId.of(settings.timezone());
                var zonedNow = ZonedDateTime.now(zoneId);
                var today = zonedNow.toLocalDate();

                if (schedule == null || !schedule.generationTime().toLocalDate().equals(today)) {
                    List<ZonedDateTime> newSchedule = generateDailySchedule(today, settings);
                    chatSchedules.put(chatId, new ChatSchedule(zonedNow, newSchedule));
                    log.info("Scheduled for {}: {}", chatId, newSchedule);
                }
            } catch (Exception e) {
                log.error("Failed to generate schedule for {}", chatId, e);
            }
        }
    }

    private List<ZonedDateTime> generateDailySchedule(LocalDate date, ScheduleSettings settings) {
        List<ZonedDateTime> schedule = new ArrayList<>();

        var zoneId = ZoneId.of(settings.timezone());

        for (int i = 0; i < settings.sessionsPerDay(); i++) {
            var randomTime = LocalTime.of(
                    random.nextInt(settings.hoursRange().from(), settings.hoursRange().to()),
                    random.nextInt(60),
                    random.nextInt(60)
            );
            var scheduleDateTime = ZonedDateTime.of(date, randomTime, zoneId);

            if (scheduleDateTime.isAfter(ZonedDateTime.now())) {
                schedule.add(scheduleDateTime);
            }
        }

        schedule.sort(ZonedDateTime::compareTo);
        return schedule;
    }

    private void checkAndOpenSessions(ZonedDateTime now) {
        for (var entry : chatSchedules.entrySet()) {
            var chatId = entry.getKey();
            var schedule = entry.getValue();

            try {
                var it = schedule.scheduledSessions().iterator();
                while (it.hasNext()) {
                    var scheduledTime = it.next();
                    if (scheduledTime.isBefore(now) && !checkChatIsBusyUseCase.isBusy(chatId)) {
                        openSessionUseCase.open(chatId, serviceId);
                        it.remove();
                        log.info("Created session for chat {}", chatId);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to open sessions for chat {}", chatId, e);
            }
        }
    }

    private void cleanupOldSchedules() {
        chatSchedules.entrySet().removeIf(entry -> {
            var zoneId = entry.getValue().generationTime().getZone();
            var today = ZonedDateTime.now(zoneId).toLocalDate();

            return !entry.getValue().generationTime().toLocalDate().equals(today);
        });
    }

    private record ChatSchedule(ZonedDateTime generationTime, List<ZonedDateTime> scheduledSessions) {}
}
