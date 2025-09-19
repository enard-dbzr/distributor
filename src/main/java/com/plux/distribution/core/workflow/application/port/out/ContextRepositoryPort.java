package com.plux.distribution.core.workflow.application.port.out;

import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ContextRepositoryPort {

    void save(@NotNull ChatId chatId, @NotNull String snapshot);

    @Nullable String getSnapshot(@NotNull ChatId chatId);

}
