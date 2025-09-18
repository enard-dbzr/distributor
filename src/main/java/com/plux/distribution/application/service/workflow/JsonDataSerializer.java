package com.plux.distribution.application.service.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.application.port.exception.DataProcessingException;

public abstract class JsonDataSerializer<T> implements DataSerializer<T> {

    private final Class<T> type;

    protected final ObjectMapper mapper = new ObjectMapper();

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
