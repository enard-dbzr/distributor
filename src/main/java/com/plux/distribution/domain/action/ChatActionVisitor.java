package com.plux.distribution.domain.action;

public interface ChatActionVisitor {
    void visit(ClearButtonsAction entity);
}
