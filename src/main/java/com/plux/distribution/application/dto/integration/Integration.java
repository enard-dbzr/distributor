package com.plux.distribution.application.dto.integration;

import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public record Integration(
        @NotNull ServiceId id,
        @NotNull ServiceWebhook webhook
) {

}
