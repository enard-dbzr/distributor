package com.plux.distribution.core.interaction.application.service;

import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import com.plux.distribution.core.interaction.application.port.in.ExecuteActionUseCase;
import com.plux.distribution.core.interaction.application.port.out.ActionExecutorPort;
import org.jetbrains.annotations.NotNull;

public class ExecuteActionService implements ExecuteActionUseCase {

    private final ActionExecutorPort actionExecutor;

    public ExecuteActionService(ActionExecutorPort actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @Override
    public void execute(@NotNull ChatAction action) {
        actionExecutor.execute(action);
    }
}
