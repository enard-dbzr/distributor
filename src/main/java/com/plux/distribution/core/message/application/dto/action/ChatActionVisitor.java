package com.plux.distribution.core.message.application.dto.action;

public interface ChatActionVisitor {
    void visit(ClearButtonsAction entity);
}
