package com.plux.distribution.core.interaction.application.port.out;

import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import org.jetbrains.annotations.NotNull;

public interface MessageSenderPort {

    void send(@NotNull InteractionId interactionId, @NotNull Participant recipient,
            @NotNull InteractionContent interactionContent);
}
