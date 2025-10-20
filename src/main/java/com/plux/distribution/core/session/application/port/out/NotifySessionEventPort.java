package com.plux.distribution.core.session.application.port.out;

import com.plux.distribution.core.session.application.dto.SessionDto;
import org.jetbrains.annotations.NotNull;

public interface NotifySessionEventPort {

    void notifyCreated(@NotNull SessionDto session);

    void notifyStarted(@NotNull SessionDto session);

    void notifyClosed(@NotNull SessionDto session);

}
