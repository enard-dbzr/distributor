package com.plux.distribution.application.service.session;

import com.plux.distribution.application.port.in.chat.GetAllChatsUseCase;
import com.plux.distribution.application.port.in.session.CreateSessionUseCase;
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
import org.jetbrains.annotations.NotNull;

public class RandomSessionInitializer implements InitSessionsStrategy {

    private final @NotNull CreateSessionUseCase createSessionUseCase;
    private final @NotNull GetAllChatsUseCase getAllChatsUseCase;
    private final @NotNull ServiceId serviceId;  // TODO: Remove this mock

    private final Map<ChatId, List<LocalDateTime>> scheduledSessions = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public RandomSessionInitializer(@NotNull CreateSessionUseCase createSessionUseCase,
            @NotNull GetAllChatsUseCase getAllChatsUseCase,
            @NotNull ServiceId serviceId) {
        this.createSessionUseCase = createSessionUseCase;
        this.getAllChatsUseCase = getAllChatsUseCase;
        this.serviceId = serviceId;
    }

    @Override
    public void initSessions() {
        generateScheduleIfNeeded();
        checkAndStartSessions(LocalDateTime.now());
    }

    private void generateScheduleIfNeeded() {
        var chats = getAllChatsUseCase.getAllChatIds();
        var today = LocalDate.now();

        for (var chatId : chats) {
            var schedule = scheduledSessions.get(chatId);

            if (schedule == null || schedule.stream().noneMatch(dt -> dt.toLocalDate().equals(today))) {
                List<LocalDateTime> newSchedule = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    var randomTime = LocalTime.of(random.nextInt(24), random.nextInt(60), random.nextInt(60));
                    var scheduleDateTime = LocalDateTime.of(today, randomTime);
                    if (scheduleDateTime.isAfter(LocalDateTime.now())) {
                        newSchedule.add(scheduleDateTime);
                    }
                }
                scheduledSessions.put(chatId, newSchedule);
                System.out.printf("Scheduled for %s: %s%n", chatId, newSchedule);
            }
        }
    }

    private void checkAndStartSessions(LocalDateTime now) {
        for (var entry : scheduledSessions.entrySet()) {
            var chatId = entry.getKey();
            var schedule = entry.getValue();

            var it = schedule.iterator();
            while (it.hasNext()) {
                var scheduledTime = it.next();
                if (!scheduledTime.isAfter(now)) {
                    createSessionUseCase.create(chatId, serviceId);
                    it.remove();
                }
            }
        }
    }
}
