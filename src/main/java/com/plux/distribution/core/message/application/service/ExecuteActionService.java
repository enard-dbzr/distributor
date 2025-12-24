package com.plux.distribution.core.message.application.service;

import com.plux.distribution.core.message.application.port.in.ExecuteActionUseCase;
import com.plux.distribution.core.message.application.port.out.ActionExecutorPort;
import com.plux.distribution.core.message.application.dto.action.ChatAction;
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
