package com.plux.distribution.application.port.out.action;

import com.plux.distribution.domain.action.ChatAction;
import org.jetbrains.annotations.NotNull;

public interface ActionExecutorPort {
    void execute(@NotNull ChatAction chatAction);
}
