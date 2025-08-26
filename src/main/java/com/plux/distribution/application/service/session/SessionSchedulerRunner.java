package com.plux.distribution.application.service.session;

import com.plux.distribution.application.port.in.session.InitSessionsStrategy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class SessionSchedulerRunner {

    private final @NotNull InitSessionsStrategy initSessionsStrategy;

    private final long tickIntervalSeconds;
    private ScheduledExecutorService executor = null;

    public SessionSchedulerRunner(@NotNull InitSessionsStrategy initSessionsStrategy, long tickIntervalSeconds) {
        this.initSessionsStrategy = initSessionsStrategy;
        this.tickIntervalSeconds = tickIntervalSeconds;
    }

    public void tick() {
        initSessionsStrategy.initSessions();
    }

    public void start() {
        if (executor != null && !executor.isShutdown()) {
            return;
        }
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(
                this::tick,
                0,
                tickIntervalSeconds,
                TimeUnit.SECONDS
        );
    }

    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}