package com.plux.distribution.domain.workflow;

import org.jetbrains.annotations.NotNull;

public record FrameEntry (Frame frame, boolean execute) {
    @NotNull FrameEntrySnapshot save() {
        return new FrameEntrySnapshot(frame.getKey(), execute);
    }

    static FrameEntry restore(@NotNull FrameEntrySnapshot snapshot, FrameFactory factory) {
        return new FrameEntry(factory.get(snapshot.frameKey()), snapshot.execute());
    }

    public record FrameEntrySnapshot(String frameKey, boolean execute) {

    }
}
