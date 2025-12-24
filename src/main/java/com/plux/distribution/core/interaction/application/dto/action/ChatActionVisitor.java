package com.plux.distribution.core.interaction.application.dto.action;

public interface ChatActionVisitor {

    void visit(ClearButtonsAction entity);
}
