package com.plux.distribution.application.workflow.frame.utils;

import com.plux.distribution.application.service.workflow.JsonDataSerializer;
import com.plux.distribution.domain.message.MessageId;

public record LastMessageData(MessageId messageId) {

    public static class Serializer extends JsonDataSerializer<LastMessageData> {

        public Serializer() {
            super(LastMessageData.class);
        }
    }
}
