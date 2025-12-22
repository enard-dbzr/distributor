package com.plux.distribution.core.workflow.application.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.workflow.domain.frame.DataSerializer;
import com.plux.distribution.core.workflow.domain.FrameContext;
import org.jetbrains.annotations.NotNull;

public abstract class JsonDataSerializer<T> implements DataSerializer<T> {

    private final Class<T> type;

    protected final ObjectMapper mapper = new ObjectMapper();

    protected JsonDataSerializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public @NotNull JsonNode serialize(@NotNull FrameContext context, @NotNull T data) {
        return mapper.valueToTree(data);
    }

    @Override
    public @NotNull T create(@NotNull FrameContext context, @NotNull JsonNode data) {
        try {
            return mapper.treeToValue(data, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
