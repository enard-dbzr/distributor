package com.plux.distribution.core.workflow.domain;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class FrameSnapshotBuilder {

    private final Map<String, PoolId> data = new HashMap<>();
    private final Map<String, Object> deepData = new HashMap<>();
    private PoolId frame;

    public FrameSnapshotBuilder setFrame(@NotNull PoolId frame) {
        this.frame = frame;
        return this;
    }

    public FrameSnapshotBuilder addData(@NotNull String key, @NotNull PoolId poolId) {
        this.data.put(key, poolId);
        return this;
    }

    public FrameSnapshotBuilder addDeepData(@NotNull String key, @NotNull Object value) {
        this.deepData.put(key, value);
        return this;
    }

    public FrameSnapshot createFrameSnapshot() {
        return new FrameSnapshot(frame, data, deepData);
    }
}