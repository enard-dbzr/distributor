package com.plux.distribution.core.workflow.application.port.in;

import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;

public interface CheckChatBusyUseCase {

    boolean isBusy(@NotNull ChatId chatId);
}
