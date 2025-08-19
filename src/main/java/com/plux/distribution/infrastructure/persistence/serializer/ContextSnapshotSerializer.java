package com.plux.distribution.infrastructure.persistence.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.application.workflow.core.FrameContext;

public class ContextSnapshotSerializer {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(FrameContext.ContextSnapshot snapshot) {
        try {
            return mapper.writeValueAsString(snapshot);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize context snapshot", e);
        }
    }

    public static FrameContext.ContextSnapshot fromJson(String json) {
        try {
            return mapper.readValue(json, FrameContext.ContextSnapshot.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize context snapshot", e);
        }
    }
}
