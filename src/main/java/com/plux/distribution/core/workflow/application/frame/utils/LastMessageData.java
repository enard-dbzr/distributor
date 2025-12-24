package com.plux.distribution.core.workflow.application.frame.utils;

import com.plux.distribution.core.workflow.application.utils.JsonDataSerializer;
import com.plux.distribution.core.message.domain.MessageId;

public record LastMessageData(MessageId messageId) {

    public static class Serializer extends JsonDataSerializer<LastMessageData> {

        public Serializer() {
            super(LastMessageData.class);
        }
    }
}
