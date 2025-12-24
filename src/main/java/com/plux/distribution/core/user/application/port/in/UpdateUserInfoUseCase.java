package com.plux.distribution.core.user.application.port.in;

import com.plux.distribution.core.user.domain.UserId;
import com.plux.distribution.core.user.domain.UserInfo;
import org.jetbrains.annotations.NotNull;

public interface UpdateUserInfoUseCase {
    void update(@NotNull UserId userId, @NotNull UserInfo userInfo);
}
