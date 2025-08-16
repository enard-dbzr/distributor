package com.plux.distribution.application.port.out.specific.telegram;

import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public interface TgUserLinker {
    void link(@NotNull UserId internal, @NotNull Long external);
}
