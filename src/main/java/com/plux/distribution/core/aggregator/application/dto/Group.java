package com.plux.distribution.core.aggregator.application.dto;

import java.time.Instant;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record Group<T>(@NotNull String key, @NotNull List<T> parts, @NotNull Instant startedAt) {}
