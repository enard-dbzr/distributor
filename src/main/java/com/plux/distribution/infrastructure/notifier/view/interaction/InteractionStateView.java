package com.plux.distribution.infrastructure.notifier.view.interaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.core.interaction.domain.InteractionState;
import com.plux.distribution.core.interaction.domain.InteractionState.Pending;
import com.plux.distribution.core.interaction.domain.InteractionState.Transferred;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = InteractionStateView.PendingStateView.class, name = "pending"),
        @JsonSubTypes.Type(value = InteractionStateView.TransferredStateView.class, name = "transferred"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = InteractionStateView.PendingStateView.class, value = "pending"),
                @DiscriminatorMapping(schema = InteractionStateView.TransferredStateView.class, value = "transferred"),
        }
)
public sealed interface InteractionStateView permits InteractionStateView.PendingStateView,
        InteractionStateView.TransferredStateView {

    static @NotNull InteractionStateView create(@NotNull InteractionState state) {
        return switch (state) {
            case Pending _ -> new PendingStateView();
            case Transferred s -> new TransferredStateView(s.transferTime());
        };
    }

    record PendingStateView() implements InteractionStateView {}

    record TransferredStateView(@NotNull Date transferTime) implements InteractionStateView {}
}
