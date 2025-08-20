package com.plux.distribution.infrastructure.persistence.serializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.plux.distribution.application.workflow.core.FrameContext;

public class ContextSnapshotSerializer {
    private final ObjectMapper mapper = new ObjectMapper();

    public ContextSnapshotSerializer() {
        mapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
    }

    public String toJson(FrameContext.ContextSnapshot snapshot) {
        try {
            return mapper.writeValueAsString(snapshot);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize context snapshot", e);
        }
    }

    public FrameContext.ContextSnapshot fromJson(String json) {
        try {
            return mapper.readValue(json, FrameContext.ContextSnapshot.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize context snapshot", e);
        }
    }
}
