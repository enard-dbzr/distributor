package com.plux.distribution.core.workflow.application.frame;

import com.plux.distribution.core.workflow.domain.Frame;
import com.plux.distribution.core.workflow.domain.FrameRegistry;
import com.plux.distribution.core.workflow.domain.FrameFactory;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class DefaultFrameRegistry implements FrameRegistry {

    private final Map<Class<? extends Frame>, String> typeToId = new HashMap<>();
    private final Map<String, FrameFactory<?>> idToFactory = new HashMap<>();

    public <T extends Frame> void register(String id, Class<T> frameClass, FrameFactory<T> factory) {
        typeToId.put(frameClass, id);
        idToFactory.put(id, factory);
    }

    @Override
    public String getFrameId(@NotNull Class<? extends Frame> frameType) {
        return typeToId.get(frameType);
    }

    @Override
    public @NotNull FrameFactory<?> getFactory(@NotNull String frameId) {
        return idToFactory.get(frameId);
    }
}
