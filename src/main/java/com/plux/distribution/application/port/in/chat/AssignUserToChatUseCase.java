package com.plux.distribution.application.port.in.chat;

import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public interface AssignUserToChatUseCase {
    void assignUser(@NotNull ChatId chatId, @NotNull UserId userId);

}
