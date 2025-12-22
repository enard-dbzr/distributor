package com.plux.distribution.core.workflow.application.serializer;

import com.plux.distribution.core.workflow.domain.frame.DataSerializer;
import com.plux.distribution.core.workflow.domain.objectpool.SerializerRegistry;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class DefaultSerializerRegistry implements SerializerRegistry {

    private final Map<Class<?>, String> typeToId = new HashMap<>();
    private final Map<String, Class<?>> idToType = new HashMap<>();
    private final Map<String, DataSerializer<?>> idToSerializer = new HashMap<>();


    public <T> void register(String id, Class<T> type, DataSerializer<T> serializer) {
        typeToId.put(type, id);
        idToType.put(id, type);
        idToSerializer.put(id, serializer);
    }

    @Override
    public <T> SerializerWithId<T> get(@NotNull Class<T> type) {
        var id = typeToId.get(type);
        //noinspection unchecked
        return new SerializerWithId<>(
                (DataSerializer<T>) idToSerializer.get(id),
                id
        );
    }

    @Override
    public <T> DataSerializer<T> findById(@NotNull String id, @NotNull Class<T> type) {
        var registeredType = idToType.get(id);

        if (!type.isAssignableFrom(registeredType)) {
            throw new IllegalArgumentException(
                    "Type mismatch: expected " + type.getName() + " but got " + registeredType.getName());
        }

        //noinspection unchecked
        return (DataSerializer<T>) idToSerializer.get(id);
    }
}
