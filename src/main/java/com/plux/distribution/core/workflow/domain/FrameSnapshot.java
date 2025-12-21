package com.plux.distribution.core.workflow.domain;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

public record FrameSnapshot(
        @NotNull PoolId frame,
        @NotNull Map<String, PoolId> data,
        @NotNull Map<String, Object> deepData
) {
}
