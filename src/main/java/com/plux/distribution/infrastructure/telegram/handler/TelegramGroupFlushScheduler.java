package com.plux.distribution.infrastructure.telegram.handler;

import com.plux.distribution.core.aggregator.application.dto.Group;
import com.plux.distribution.core.aggregator.application.port.in.FlushReadyGroupsUseCase;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class TelegramGroupFlushScheduler {

    private static final Logger log = LogManager.getLogger(TelegramGroupFlushScheduler.class);
    private final @NotNull FlushReadyGroupsUseCase<Message> flushReadyGroupsUseCase;
    private final @NotNull UpdateProcessor updateProcessor;
    private final @NotNull ScheduledExecutorService scheduler;

    private final Duration interval;

    public TelegramGroupFlushScheduler(
            @NotNull FlushReadyGroupsUseCase<Message> flushReadyGroupsUseCase,
            @NotNull UpdateProcessor updateProcessor,
            @NotNull Duration interval
    ) {
        this.flushReadyGroupsUseCase = flushReadyGroupsUseCase;
        this.updateProcessor = updateProcessor;
        this.interval = interval;
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "tg-group-flush");
            t.setDaemon(true);
            return t;
        });
    }

    public void start() {
        scheduler.scheduleWithFixedDelay(
                this::flushOnceSafe,
                interval.toMillis(),
                interval.toMillis(),
                TimeUnit.MILLISECONDS
        );
    }


    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void flushOnceSafe() {
        try {
            List<Group<Message>> groups = flushReadyGroupsUseCase.flushReadyGroups();
            for (Group<Message> group : groups) {
                updateProcessor.processMessageGroup(group.parts());
            }
        } catch (Exception e) {
            log.error("Failed to flush groups", e);
        }
    }
}
