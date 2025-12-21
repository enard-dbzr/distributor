package com.plux.distribution.core.workflow.application.serializer;

import com.plux.distribution.core.interaction.domain.InteractionId;

public class InteractionIdDataSerializer extends JsonDataSerializer<InteractionId> {

    public InteractionIdDataSerializer() {
        super(InteractionId.class);
    }
}
