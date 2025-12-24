package com.plux.distribution.core.interaction.domain;

import java.util.Date;
import org.jetbrains.annotations.NotNull;

public sealed interface InteractionState {

    boolean equals(Object other);

    record Pending() implements InteractionState {}

    record Transferred(@NotNull Date transferTime) implements InteractionState {}
}
