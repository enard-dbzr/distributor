package com.plux.distribution.application.service.session;

import com.plux.distribution.application.port.in.chat.GetAllChatsUseCase;
import com.plux.distribution.application.port.in.session.OpenSessionUseCase;
import com.plux.distribution.application.port.in.session.InitSessionsStrategy;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.service.ServiceId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class RandomSessionInitializer implements InitSessionsStrategy {

    private static final Logger log = LogManager.getLogger(RandomSessionInitializer.class);

    private final @NotNull OpenSessionUseCase openSessionUseCase;
    private final @NotNull GetAllChatsUseCase getAllChatsUseCase;
    private final @NotNull ServiceId serviceId;  // TODO: Remove this mock

    private final Map<ChatId, ChatSchedule> chatSchedules = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public RandomSessionInitializer(@NotNull OpenSessionUseCase openSessionUseCase,
            @NotNull GetAllChatsUseCase getAllChatsUseCase,
            @NotNull ServiceId serviceId) {
        this.openSessionUseCase = openSessionUseCase;
        this.getAllChatsUseCase = getAllChatsUseCase;
        this.serviceId = serviceId;
    }

    @Override
    public void initSessions() {
        generateScheduleIfNeeded();
        checkAndOpenSessions(LocalDateTime.now());
        cleanupOldSchedules();
    }

    private void generateScheduleIfNeeded() {
        var chats = getAllChatsUseCase.getAllChatIds();
        var today = LocalDate.now();

        for (var chatId : chats) {
            var schedule = chatSchedules.get(chatId);

            if (schedule == null || !schedule.generationDate().equals(today)) {
                List<LocalDateTime> newSchedule = generateDailySchedule(today);
                chatSchedules.put(chatId, new ChatSchedule(today, newSchedule));
                log.info("Scheduled for {}: {}", chatId, newSchedule);
            }
        }
    }

    private List<LocalDateTime> generateDailySchedule(LocalDate date) {
        List<LocalDateTime> schedule = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            var randomTime = LocalTime.of(random.nextInt(24), random.nextInt(60), random.nextInt(60));
            var scheduleDateTime = LocalDateTime.of(date, randomTime);

            if (scheduleDateTime.isAfter(LocalDateTime.now())) {
                schedule.add(scheduleDateTime);
            }
        }

        schedule.sort(LocalDateTime::compareTo);
        return schedule;
    }

    private void checkAndOpenSessions(LocalDateTime now) {
        for (var entry : chatSchedules.entrySet()) {
            var chatId = entry.getKey();
            var schedule = entry.getValue();

            var it = schedule.scheduledSessions().iterator();
            while (it.hasNext()) {
                var scheduledTime = it.next();
                if (scheduledTime.isBefore(now)) {
                    openSessionUseCase.open(chatId, serviceId);
                    it.remove();
                    log.info("Created session for chat {}", chatId);
                }
            }
        }
    }

    private void cleanupOldSchedules() {
        var today = LocalDate.now();
        chatSchedules.entrySet().removeIf(entry ->
                !entry.getValue().generationDate().equals(today));
    }

    private record ChatSchedule(LocalDate generationDate, List<LocalDateTime> scheduledSessions) {

    }
}
