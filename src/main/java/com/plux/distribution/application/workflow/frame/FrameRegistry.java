package com.plux.distribution.application.workflow.frame;

import com.plux.distribution.application.workflow.core.Frame;
import com.plux.distribution.application.workflow.core.FrameFactory;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class FrameRegistry implements FrameFactory {
    private final Map<String, Frame> container = new HashMap<>();

    public void register(Frame frame) {
        container.put(frame.getKey(), frame);
    }

    @Override
    public @NotNull Frame get(@NotNull String key) {
        var frame = container.get(key);
        if (frame == null) {
            throw new IllegalArgumentException("Frame with key=%s was not found".formatted(key));
        }
        return frame;
    }
}
