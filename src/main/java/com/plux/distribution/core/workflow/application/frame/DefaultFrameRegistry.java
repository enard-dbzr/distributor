package com.plux.distribution.core.workflow.application.frame;

import com.plux.distribution.core.workflow.application.port.out.FrameRegistry;
import com.plux.distribution.core.workflow.domain.Frame;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class DefaultFrameRegistry implements FrameRegistry {

    private final Map<String, Frame> idToFrame = new HashMap<>();
    private final Map<Class<? extends Frame>, String> typeToId = new HashMap<>();

    public void register(String id, Frame frame) {
        idToFrame.put(id, frame);
        typeToId.put(frame.getClass(), id);
    }

    @Override
    public @NotNull Frame get(@NotNull String key) {
        var frame = idToFrame.get(key);
        if (frame == null) {
            throw new IllegalArgumentException("Frame with key=%s was not found".formatted(key));
        }
        return frame;
    }

    @Override
    public @NotNull String idByType(@NotNull Class<? extends Frame> type) {
        return typeToId.get(type);
    }
}
