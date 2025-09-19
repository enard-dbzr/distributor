package com.plux.distribution.infrastructure.notifier.view.message.state;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.core.message.domain.state.MessageState;
import com.plux.distribution.core.message.domain.state.MessageStateVisitor;
import com.plux.distribution.core.message.domain.state.PendingState;
import com.plux.distribution.core.message.domain.state.ReceivedState;
import com.plux.distribution.core.message.domain.state.TransferredState;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = PendingStateView.class, value = "pending"),
                @DiscriminatorMapping(schema = ReceivedStateView.class, value = "received"),
                @DiscriminatorMapping(schema = TransferredStateView.class, value = "transferred"),
        }
)
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
