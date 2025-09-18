package com.plux.distribution.application.workflow.frame;

import com.plux.distribution.application.service.workflow.DataRegistry;
import com.plux.distribution.application.service.workflow.DataKey;
import com.plux.distribution.application.service.workflow.DataSerializer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public class DefaultDataRegistry implements DataRegistry {

    private final Map<String, DataKey<?>> idToKey = new ConcurrentHashMap<>();
    private final Map<Class<?>, DataKey<?>> typeToKey = new ConcurrentHashMap<>();

    public <T> void register(@NotNull String id, @NotNull Class<T> type, @NotNull DataSerializer<T> serializer) {
        var key = new DataKey<>(id, type, serializer);
        idToKey.put(id, key);
        typeToKey.put(type, key);
    }

    @Override
    public @NotNull <T> DataKey<T> keyByType(Class<T> type) {
        //noinspection unchecked
        return (DataKey<T>) typeToKey.get(type);
    }

    @Override
    public @NotNull DataKey<?> keyById(String id) {
        return idToKey.get(id);
    }
}
