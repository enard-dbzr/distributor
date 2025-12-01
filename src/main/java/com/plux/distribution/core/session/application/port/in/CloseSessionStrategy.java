package com.plux.distribution.core.session.application.port.in;

import com.plux.distribution.core.feedback.application.dto.Feedback;
import com.plux.distribution.core.session.application.dto.SessionDto;
import org.jetbrains.annotations.NotNull;

public interface CloseSessionStrategy {

    boolean isCloseNeeded(@NotNull SessionDto session, @NotNull Feedback feedback);
}
