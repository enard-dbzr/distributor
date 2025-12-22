package com.plux.distribution.core.workflow.application.frame.utils.data;

import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.workflow.application.serializer.JsonDataSerializer;

public class InteractionIdDataSerializer extends JsonDataSerializer<InteractionId> {

    public InteractionIdDataSerializer() {
        super(InteractionId.class);
    }
}
