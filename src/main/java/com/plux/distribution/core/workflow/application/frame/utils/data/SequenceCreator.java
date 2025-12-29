package com.plux.distribution.core.workflow.application.frame.utils.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.plux.distribution.core.workflow.application.frame.utils.SequenceFrame;
import com.plux.distribution.core.workflow.domain.FrameContext;
import com.plux.distribution.core.workflow.domain.frame.DataSerializer;
import com.plux.distribution.core.workflow.domain.frame.Frame;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class SequenceCreator implements DataSerializer<SequenceFrame> {

    private final List<DataSerializer<Frame>> subFactories;

    public SequenceCreator(List<DataSerializer<Frame>> subFactories) {
        this.subFactories = subFactories;
    }

    @Override
    public @NotNull JsonNode serialize(@NotNull FrameContext context, @NotNull SequenceFrame data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull SequenceFrame create(@NotNull FrameContext context, @NotNull JsonNode data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull SequenceFrame create(@NotNull FrameContext context) {
        return new SequenceFrame(
                subFactories.stream()
                        .map(f -> f.create(context))
                        .toList()

        );
    }
}
