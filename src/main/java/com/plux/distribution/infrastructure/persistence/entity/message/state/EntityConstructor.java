package com.plux.distribution.infrastructure.persistence.entity.message.state;

import com.plux.distribution.domain.message.state.MessageStateVisitor;
import com.plux.distribution.domain.message.state.PendingState;
import com.plux.distribution.domain.message.state.TransferredState;

class EntityConstructor implements MessageStateVisitor {
    MessageStateEntity result;

    @Override
    public void visit(PendingState state) {
        result = PendingStateEntity.fromModel(state);
    }

    @Override
    public void visit(TransferredState state) {
        result = TransferredStateEntity.fromModel(state);
    }
}
