package com.plux.distribution.application.service.workflow;

import com.fasterxml.jackson.databind.JsonNode;
import com.plux.distribution.application.port.exception.DataProcessingException;

public interface DataSerializer<T> {

    JsonNode serialize(T data);

    T deserialize(JsonNode data) throws DataProcessingException;
}
