package com.plux.distribution.application.port.out.session;

import com.plux.distribution.application.dto.session.SessionDto;
import org.jetbrains.annotations.NotNull;

public interface NotifySessionEventPort {

    void notifyCreated(@NotNull SessionDto session);

    void notifyStarted(@NotNull SessionDto session);

}
