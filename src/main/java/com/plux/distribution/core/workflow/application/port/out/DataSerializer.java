package com.plux.distribution.core.workflow.application.port.out;

import com.fasterxml.jackson.databind.JsonNode;
import com.plux.distribution.core.workflow.application.exception.DataProcessingException;

public interface DataSerializer<T> {

    JsonNode serialize(T data);

    T deserialize(JsonNode data) throws DataProcessingException;
}
