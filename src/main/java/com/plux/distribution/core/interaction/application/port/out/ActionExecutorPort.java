package com.plux.distribution.core.interaction.application.port.out;

import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import org.jetbrains.annotations.NotNull;

public interface ActionExecutorPort {

    void execute(@NotNull ChatAction chatAction);
}
