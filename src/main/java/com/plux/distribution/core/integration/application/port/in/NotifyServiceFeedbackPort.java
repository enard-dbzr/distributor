package com.plux.distribution.core.integration.application.port.in;

import com.plux.distribution.core.feedback.application.dto.ResolvedFeedback;
import com.plux.distribution.core.integration.domain.ServiceId;
import org.jetbrains.annotations.NotNull;

public interface NotifyServiceFeedbackPort {
    void notifyGotFeedback(@NotNull ServiceId serviceId, @NotNull ResolvedFeedback resolvedFeedback);
}
