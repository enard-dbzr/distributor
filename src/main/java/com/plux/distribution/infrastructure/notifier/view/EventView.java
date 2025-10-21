package com.plux.distribution.infrastructure.notifier.view;

import com.plux.distribution.infrastructure.notifier.view.feedback.UserInteractionView;
import com.plux.distribution.infrastructure.notifier.view.session.SessionEvent;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

public record EventView(
        @NotNull EventType eventType,

        @Schema(
                description = "Payload depends on eventType",
                oneOf = {SessionEvent.class, UserInteractionView.class},
                discriminatorProperty = "eventType",
                discriminatorMapping = {
                        @DiscriminatorMapping(value = "SESSION", schema = SessionEvent.class),
                        @DiscriminatorMapping(value = "FEEDBACK", schema = UserInteractionView.class)
                }
        )
        Object data
) {

}
