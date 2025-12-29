package com.plux.distribution.core.workflow.application.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.frame.DataSerializer;
import com.plux.distribution.core.workflow.domain.FrameContext;
import org.jetbrains.annotations.NotNull;

public abstract class PoolAwareSerializer<T> implements DataSerializer<T> {

    protected final ObjectMapper mapper = new ObjectMapper();

    @Override
    public final @NotNull JsonNode serialize(@NotNull FrameContext context, @NotNull T data) {
        PoolNodeSnapshotBuilder snapshotBuilder = PoolNodeSnapshot.builder();

        return mapper.valueToTree(buildSnapshot(context, data, snapshotBuilder).build());
    }

    @Override
    public final @NotNull T create(@NotNull FrameContext context, @NotNull JsonNode data) {
        try {
            var snapshotNode = mapper.treeToValue(data, PoolNodeSnapshot.class);

            return create(context, snapshotNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public PoolNodeSnapshotBuilder buildSnapshot(@NotNull FrameContext context, T instance,
            PoolNodeSnapshotBuilder builder) {
        return builder;
    }

    public abstract T create(@NotNull FrameContext context, PoolNodeSnapshot snapshot);
}
