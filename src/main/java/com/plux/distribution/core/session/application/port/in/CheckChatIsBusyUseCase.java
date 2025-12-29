package com.plux.distribution.core.session.application.port.in;

import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;

public interface CheckChatIsBusyUseCase {

    boolean isBusy(@NotNull ChatId chatId);
}
