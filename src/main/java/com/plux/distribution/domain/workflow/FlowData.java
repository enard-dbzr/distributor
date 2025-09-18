package com.plux.distribution.domain.workflow;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;

public class FlowData {

    private final Map<Class<?>, Object> data = new ConcurrentHashMap<>();

    public <T> void put(Class<T> type, T value) {
        data.put(type, value);
    }

    public <T> T get(Class<T> type) {
        return type.cast(data.get(type));
    }

    public <T> boolean contains(Class<T> type) {
        return data.containsKey(type);
    }

    public <T> void remove(Class<T> type) {
        data.remove(type);
    }

    public void clear() {
        data.clear();
    }

    @NotNull Map<Class<?>, Object> save() {
        return new HashMap<>(data);
    }

    void restore(@NotNull Map<Class<?>, Object> snapshot) {
        data.clear();
        data.putAll(snapshot);
    }
}
