package com.plux.distribution.core.session.domain;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.integration.domain.ServiceId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Session {

    private final @NotNull SessionId id;
    private final @NotNull ChatId chatId;
    private final @NotNull ServiceId serviceId;
    private @NotNull SessionState state;
    private final @NotNull Date openTime;
    private @Nullable Date closeTime;

    public Session(@NotNull SessionId id, @NotNull ChatId chatId, @NotNull ServiceId serviceId,
            @NotNull SessionState state,
            @NotNull Date openTime, @Nullable Date closeTime) {
        this.id = id;
        this.chatId = chatId;
        this.serviceId = serviceId;
        this.state = state;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public void start() {
        this.state = SessionState.STARTED;
    }

    public void close(@NotNull Date closeTime) {
        this.state = SessionState.CLOSED;
        this.closeTime = closeTime;
    }

    public @NotNull SessionId getId() {
        return id;
    }

    public @NotNull ChatId getChatId() {
        return chatId;
    }

    public @NotNull ServiceId getServiceId() {
        return serviceId;
    }

    public @NotNull SessionState getState() {
        return state;
    }

    public @NotNull Date getOpenTime() {
        return openTime;
    }

    public @Nullable Date getCloseTime() {
        return closeTime;
    }
}
