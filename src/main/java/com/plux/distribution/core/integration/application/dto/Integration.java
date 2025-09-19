package com.plux.distribution.core.integration.application.dto;

import com.plux.distribution.core.integration.domain.ServiceId;
import org.jetbrains.annotations.NotNull;

public record Integration(
        @NotNull ServiceId id,
        @NotNull ServiceWebhook webhook
) {

}
