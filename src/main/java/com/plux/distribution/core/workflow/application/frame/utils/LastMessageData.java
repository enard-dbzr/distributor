package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.core.workflow.application.utils.JsonDataSerializer;

public record LastMessageData(InteractionId interactionId) {

    public static class Serializer extends JsonDataSerializer<LastMessageData> {

        public Serializer() {
            super(LastMessageData.class);
        }
    }
}
