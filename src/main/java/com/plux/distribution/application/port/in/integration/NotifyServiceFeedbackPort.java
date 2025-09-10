package com.plux.distribution.application.port.in.integration;

import com.plux.distribution.application.dto.feedback.dto.ResolvedFeedback;
import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public interface NotifyServiceFeedbackPort {
    void notifyGotFeedback(@NotNull ServiceId serviceId, @NotNull ResolvedFeedback resolvedFeedback);
}
