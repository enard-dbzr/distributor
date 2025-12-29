package com.plux.distribution.core.session.application.service;

import com.plux.distribution.core.chat.application.port.in.GetChatUseCase;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.session.application.port.in.CheckChatIsBusyUseCase;
import com.plux.distribution.core.workflow.application.port.in.CheckWorkflowIsEmptyUseCase;
import org.jetbrains.annotations.NotNull;

public class CheckChatIsBusyService implements CheckChatIsBusyUseCase {

    private final @NotNull GetChatUseCase getChatUseCase;
    private final @NotNull CheckWorkflowIsEmptyUseCase checkWorkflowIsEmptyUseCase;

    public CheckChatIsBusyService(@NotNull GetChatUseCase getChatUseCase,
            @NotNull CheckWorkflowIsEmptyUseCase checkWorkflowIsEmptyUseCase) {
        this.getChatUseCase = getChatUseCase;
        this.checkWorkflowIsEmptyUseCase = checkWorkflowIsEmptyUseCase;
    }

    @Override
    public boolean isBusy(@NotNull ChatId chatId) {
        return getChatUseCase.get(chatId).userId() == null ||
                !checkWorkflowIsEmptyUseCase.isEmpty(chatId);
    }
}
