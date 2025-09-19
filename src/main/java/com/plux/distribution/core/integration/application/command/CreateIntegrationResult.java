package com.plux.distribution.core.integration.application.command;

import com.plux.distribution.core.integration.application.dto.ServiceToken;
import com.plux.distribution.core.integration.domain.ServiceId;
import org.jetbrains.annotations.NotNull;

public record CreateIntegrationResult(
        @NotNull ServiceToken token,
        @NotNull ServiceId id
) {

}
