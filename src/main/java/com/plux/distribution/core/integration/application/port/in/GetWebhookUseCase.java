package com.plux.distribution.core.integration.application.port.in;

import com.plux.distribution.core.integration.application.dto.ServiceWebhook;
import com.plux.distribution.core.integration.domain.ServiceId;
import org.jetbrains.annotations.NotNull;

public interface GetWebhookUseCase {

    @NotNull ServiceWebhook getWebhook(@NotNull ServiceId serviceId);
}
