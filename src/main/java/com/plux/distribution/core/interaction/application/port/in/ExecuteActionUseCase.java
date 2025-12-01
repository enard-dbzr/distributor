package com.plux.distribution.core.interaction.application.port.in;

import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import org.jetbrains.annotations.NotNull;

public interface ExecuteActionUseCase {

    void execute(@NotNull ChatAction action);
}
