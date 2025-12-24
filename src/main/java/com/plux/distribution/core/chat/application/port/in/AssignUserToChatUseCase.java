package com.plux.distribution.core.chat.application.port.in;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.user.domain.UserId;
import org.jetbrains.annotations.NotNull;

public interface AssignUserToChatUseCase {
    void assignUser(@NotNull ChatId chatId, @NotNull UserId userId);

}
