package com.plux.distribution.core.workflow.application.utils;

import com.plux.distribution.core.interaction.application.command.DeliverInteractionCommand;
import com.plux.distribution.core.interaction.application.dto.action.ChatAction;
import com.plux.distribution.core.interaction.application.port.in.ExecuteActionUseCase;
import com.plux.distribution.core.interaction.application.port.in.InteractionDeliveryUseCase;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.content.InteractionContent;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameContextManager;
import org.jetbrains.annotations.NotNull;

public class DefaultContextManager implements FrameContextManager {

    public final @NotNull InteractionDeliveryUseCase deliveryUseCase;
    public final @NotNull ExecuteActionUseCase executeActionUseCase;

    public DefaultContextManager(@NotNull InteractionDeliveryUseCase deliveryUseCase,
            @NotNull ExecuteActionUseCase executeActionUseCase) {
        this.deliveryUseCase = deliveryUseCase;
        this.executeActionUseCase = executeActionUseCase;
    }

    @Override
    public InteractionId send(FrameContext context, InteractionContent message) {
        return deliveryUseCase.deliver(new DeliverInteractionCommand(
                new BotParticipant(),
                new ChatParticipant(context.getChatId()),
                message
        ));
    }

    @Override
    public void dispatch(FrameContext context, ChatAction action) {
        executeActionUseCase.execute(action);
    }
}
