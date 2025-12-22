package com.plux.distribution.core.workflow.domain.frame;

import com.fasterxml.jackson.databind.JsonNode;
import com.plux.distribution.core.workflow.domain.FrameContext;
import org.jetbrains.annotations.NotNull;

public interface DataSerializer<T> {

    @NotNull JsonNode serialize(@NotNull FrameContext context, @NotNull T data);

    @NotNull T create(@NotNull FrameContext context, @NotNull JsonNode data);
}
