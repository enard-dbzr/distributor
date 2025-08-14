package com.plux.distribution.application.port.out.specific.telegram;

import com.plux.distribution.domain.message.MessageId;
import org.jetbrains.annotations.NotNull;

public interface TgMessageLinker {
    void link(@NotNull MessageId internal, @NotNull Integer external);
}
