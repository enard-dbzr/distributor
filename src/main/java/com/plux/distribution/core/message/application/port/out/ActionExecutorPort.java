package com.plux.distribution.core.message.application.port.out;

import com.plux.distribution.core.message.application.dto.action.ChatAction;
import org.jetbrains.annotations.NotNull;

public interface ActionExecutorPort {
    void execute(@NotNull ChatAction chatAction);
}
