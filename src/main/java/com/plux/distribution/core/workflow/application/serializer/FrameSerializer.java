package com.plux.distribution.core.workflow.application.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plux.distribution.core.workflow.domain.DataSerializer;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.FrameFactory;
import com.plux.distribution.core.workflow.domain.FrameRegistry;
import com.plux.distribution.core.workflow.domain.FrameSnapshot;
import org.jetbrains.annotations.NotNull;

public class FrameSerializer implements DataSerializer<Frame> {

    private final FrameRegistry frameRegistry;

    protected final ObjectMapper mapper = new ObjectMapper();

    public FrameSerializer(FrameRegistry frameRegistry) {
        this.frameRegistry = frameRegistry;
    }

    @Override
    public JsonNode serialize(@NotNull FrameContext context, Frame data) {

        var frameId = frameRegistry.getFrameId(data.getClass());
        @SuppressWarnings("unchecked")
        FrameFactory<Frame> factory = (FrameFactory<Frame>) frameRegistry.getFactory(frameId);

        var snapshot = factory.save(context, data);

        return mapper.valueToTree(new SnapshotWithDescriptor(snapshot, frameId));
    }

    @Override
    public Frame create(FrameContext context, JsonNode data) {
        try {
            var value = mapper.treeToValue(data, SnapshotWithDescriptor.class);
            var factory = frameRegistry.getFactory(value.frameId());
            return factory.create(context, value.snapshot());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private record SnapshotWithDescriptor(FrameSnapshot snapshot, String frameId) {}
}
