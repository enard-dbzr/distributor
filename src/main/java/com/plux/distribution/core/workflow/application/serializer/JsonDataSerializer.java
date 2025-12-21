package com.plux.distribution.core.workflow.application.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.workflow.domain.DataSerializer;
import com.plux.distribution.core.workflow.domain.FrameContext;
import org.jetbrains.annotations.NotNull;

public abstract class JsonDataSerializer<T> implements DataSerializer<T> {

    private final Class<T> type;

    protected final ObjectMapper mapper = new ObjectMapper();

    protected JsonDataSerializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public JsonNode serialize(@NotNull FrameContext context, T data) {
        return mapper.valueToTree(data);
    }

    @Override
    public T create(FrameContext context, JsonNode data) {
        try {
            return mapper.treeToValue(data, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void restore(FrameContext context, T instance, JsonNode data) {

    }
}
