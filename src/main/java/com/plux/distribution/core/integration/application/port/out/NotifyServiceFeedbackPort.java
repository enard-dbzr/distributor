package com.plux.distribution.core.integration.application.port.out;

import com.plux.distribution.core.feedback.domain.ResolvedFeedback;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.session.application.dto.SessionDto;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NotifyServiceFeedbackPort {

    void notifyGotFeedback(@NotNull ServiceId serviceId, @NotNull ResolvedFeedback resolvedFeedback,
            @Nullable SessionDto session);
}
