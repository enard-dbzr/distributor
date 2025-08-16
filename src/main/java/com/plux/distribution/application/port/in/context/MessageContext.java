package com.plux.distribution.application.port.in.context;

import com.plux.distribution.application.port.exception.UserIdNotFound;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.user.UserId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public interface MessageContext {

    @NotNull UserId getUserId() throws UserIdNotFound;

    MessageId getReplyTo();

    @NotNull String getText();

    @NotNull Date getTimestamp();

    void onMessageCreated(@NotNull MessageId messageId);

    void onUserCreated(@NotNull UserId userId);
}
