package com.plux.distribution.core.aggregator.application.service;

import com.plux.distribution.core.aggregator.application.dto.Group;
import com.plux.distribution.core.aggregator.application.port.in.AddGroupPartUseCase;
import com.plux.distribution.core.aggregator.application.port.in.FlushReadyGroupsUseCase;
import com.plux.distribution.core.aggregator.application.port.out.GroupBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AggregatorService<T> implements AddGroupPartUseCase<T>, FlushReadyGroupsUseCase<T> {

    private final GroupBuffer<T> buffer;
    private final Duration window;

    public AggregatorService(GroupBuffer<T> buffer, Duration window) {
        this.buffer = buffer;
        this.window = window;
    }

    @Override
    public void addPart(@NotNull String groupKey, @NotNull T part) {
        Instant now = Instant.now();
        buffer.add(groupKey, part, now);
    }

    @Override
    public List<Group<T>> flushReadyGroups() {
        Instant now = Instant.now();
        Instant cutoff = now.minus(window);
        return buffer.popGroupsBefore(cutoff);
    }
}
