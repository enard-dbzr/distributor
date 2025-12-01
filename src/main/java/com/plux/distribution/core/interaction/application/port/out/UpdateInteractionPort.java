package com.plux.distribution.core.interaction.application.port.out;

import com.plux.distribution.core.interaction.domain.Interaction;
import org.jetbrains.annotations.NotNull;

public interface UpdateInteractionPort {

    void update(@NotNull Interaction message);
}
