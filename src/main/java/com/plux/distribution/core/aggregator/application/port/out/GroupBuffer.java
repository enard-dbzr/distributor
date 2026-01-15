package com.plux.distribution.core.aggregator.application.port.out;

import com.plux.distribution.core.aggregator.application.dto.Group;
import java.time.Instant;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GroupBuffer<T> {

    void add(@NotNull String groupKey, @NotNull T part, @NotNull Instant groupTime);

    @NotNull List<Group<T>> popGroupsBefore(@NotNull Instant cutoff);
}
