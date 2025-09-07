package com.plux.distribution.application.port.in.integration;

import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.service.ServiceId;
import org.jetbrains.annotations.NotNull;

public interface NotifyServiceFeedbackPort {
    void notifyGotFeedback(@NotNull ServiceId serviceId, @NotNull Feedback feedback);
}
