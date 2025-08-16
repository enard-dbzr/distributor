package com.plux.distribution.application.port.in.context;

import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public interface ButtonContext {
    @NotNull UserId getUserId();
    @NotNull MessageId getReplyTo();
    @NotNull String getTag();
}
