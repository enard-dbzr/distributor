package com.plux.distribution.core.workflow.application.serializer;

import com.plux.distribution.core.workflow.domain.DataSerializer;
import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.SerializerRegistry;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class DefaultSerializerRegistry implements SerializerRegistry {

    private final @NotNull FrameSerializer frameSerializer;

    private final Map<Class<?>, DataSerializer<?>> serializerMap = new HashMap<>();

    public DefaultSerializerRegistry(@NotNull FrameSerializer frameSerializer) {
        this.frameSerializer = frameSerializer;
    }

    public <T> void register(Class<T> type, DataSerializer<T> serializer) {
        serializerMap.put(type, serializer);
    }

    @Override
    public <T> DataSerializer<T> get(Class<T> type) {
        if (Frame.class.isAssignableFrom(type)) {
            //noinspection unchecked
            return (DataSerializer<T>) frameSerializer;
        }
        //noinspection unchecked
        return (DataSerializer<T>) serializerMap.get(type);
    }
}
