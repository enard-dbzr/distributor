package com.plux.distribution.core.workflow.application.serializer;

import com.plux.distribution.core.workflow.domain.objectpool.PoolId;
import java.util.HashMap;
import java.util.Map;

public record PoolNodeSnapshot(Map<String, PoolId> values, Map<String, PoolNodeSnapshot> children) {

    public static PoolNodeSnapshotBuilder builder() {
        return new PoolNodeSnapshotBuilder();
    }

    public static class PoolNodeSnapshotBuilder {
        private final Map<String, PoolId> values = new HashMap<>();
        private final Map<String, PoolNodeSnapshot> children = new HashMap<>();

        public PoolNodeSnapshotBuilder value(String name, PoolId value) {
            values.put(name, value);
            return this;
        }

        public PoolNodeSnapshotBuilder child(String name, PoolNodeSnapshot value) {
            children.put(name, value);
            return this;
        }

        public PoolNodeSnapshot build() {
            return new PoolNodeSnapshot(values, children);
        }

    }
}
