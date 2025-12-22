package com.plux.distribution.core.workflow.application.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.workflow.application.serializer.PoolNodeSnapshot.PoolNodeSnapshotBuilder;
import com.plux.distribution.core.workflow.domain.DataSerializer;
import com.plux.distribution.core.workflow.domain.FrameContext;
import org.jetbrains.annotations.NotNull;

public abstract class PoolAwareSerializer<T> implements DataSerializer<T> {

    protected final ObjectMapper mapper = new ObjectMapper();

    @Override
    public final JsonNode serialize(@NotNull FrameContext context, T data) {
        PoolNodeSnapshotBuilder snapshotBuilder = PoolNodeSnapshot.builder();

        return mapper.valueToTree(buildFrameSnapshot(context, data, snapshotBuilder).build());
    }

    @Override
    public final T create(FrameContext context, JsonNode data) {
        try {
            var snapshotNode = mapper.treeToValue(data, PoolNodeSnapshot.class);

            return create(context, snapshotNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public PoolNodeSnapshotBuilder buildFrameSnapshot(@NotNull FrameContext context, T instance,
            PoolNodeSnapshotBuilder builder) {
        return builder;
    }

    public abstract T create(@NotNull FrameContext context, PoolNodeSnapshot snapshot);
}
