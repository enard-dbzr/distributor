package com.plux.distribution.infrastructure.notifier.view.message.state;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.domain.message.state.MessageState;
import com.plux.distribution.domain.message.state.MessageStateVisitor;
import com.plux.distribution.domain.message.state.PendingState;
import com.plux.distribution.domain.message.state.ReceivedState;
import com.plux.distribution.domain.message.state.TransferredState;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PendingStateView.class, name = "pending"),
        @JsonSubTypes.Type(value = ReceivedStateView.class, name = "received"),
        @JsonSubTypes.Type(value = TransferredStateView.class, name = "transferred"),
})
public sealed interface MessageStateView permits PendingStateView, ReceivedStateView, TransferredStateView {
    static @NotNull MessageStateView create(@NotNull MessageState state) {
        return state.accept(new MessageStateVisitor<>() {
            @Override
            public MessageStateView visit(PendingState state) {
                return new PendingStateView();
            }

            @Override
            public MessageStateView visit(TransferredState state) {
                return new TransferredStateView(state.transferTime());
            }

            @Override
            public MessageStateView visit(ReceivedState state) {
                return new ReceivedStateView(state.receiveTime());
            }
        });
    }
}
