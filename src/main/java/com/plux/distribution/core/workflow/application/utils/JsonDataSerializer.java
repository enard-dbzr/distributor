package com.plux.distribution.core.workflow.application.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.workflow.application.exception.DataProcessingException;
import com.plux.distribution.core.workflow.application.port.out.DataSerializer;

public abstract class JsonDataSerializer<T> implements DataSerializer<T> {

    protected final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> type;

    protected JsonDataSerializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public JsonNode serialize(T data) {
        return mapper.valueToTree(data);
    }

    @Override
    public T deserialize(JsonNode data) throws DataProcessingException {
        try {
            return mapper.treeToValue(data, type);
        } catch (JsonProcessingException e) {
            throw new DataProcessingException(e.getMessage());
        }
    }
}
