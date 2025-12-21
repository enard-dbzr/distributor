package com.plux.distribution.core.workflow.domain;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

public interface DataSerializer<T> {
    JsonNode serialize(@NotNull FrameContext context, T data);

    T create(FrameContext context, JsonNode data);

    void restore(FrameContext context, T instance, JsonNode data);
}
