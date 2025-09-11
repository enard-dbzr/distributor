package com.plux.distribution.application.dto.integration;

import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public record CreateIntegrationResult(
        @NotNull ServiceToken token,
        @NotNull ServiceId id
) {

}
