package com.plux.distribution.application.port.out.workflow;

import com.plux.distribution.domain.chat.ChatId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ContextRepositoryPort {

    void save(@NotNull ChatId chatId, @NotNull String snapshot);

    @Nullable String getSnapshot(@NotNull ChatId chatId);

}
