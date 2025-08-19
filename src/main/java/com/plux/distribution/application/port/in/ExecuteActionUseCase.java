package com.plux.distribution.application.port.in;

import com.plux.distribution.domain.action.ChatAction;
import org.jetbrains.annotations.NotNull;

public interface ExecuteActionUseCase {
    void execute(@NotNull ChatAction action);
}
