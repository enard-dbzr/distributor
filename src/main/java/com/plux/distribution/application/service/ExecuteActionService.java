package com.plux.distribution.application.service;

import com.plux.distribution.application.port.in.ExecuteActionUseCase;
import com.plux.distribution.application.port.out.action.ActionExecutorPort;
import com.plux.distribution.domain.action.ChatAction;
import org.jetbrains.annotations.NotNull;

public class ExecuteActionService implements ExecuteActionUseCase {
    private final ActionExecutorPort  actionExecutor;

    public ExecuteActionService(ActionExecutorPort actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @Override
    public void execute(@NotNull ChatAction action) {
        actionExecutor.execute(action);
    }
}
