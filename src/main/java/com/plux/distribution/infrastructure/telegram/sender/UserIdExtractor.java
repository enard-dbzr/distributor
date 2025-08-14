package com.plux.distribution.infrastructure.telegram.sender;

import com.plux.distribution.domain.message.participant.ParticipantVisitor;
import com.plux.distribution.domain.message.participant.SelfParticipant;
import com.plux.distribution.domain.message.participant.ServiceParticipant;
import com.plux.distribution.domain.message.participant.UnknownServiceParticipant;
import com.plux.distribution.domain.message.participant.UserParticipant;
import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

class UserIdExtractor implements ParticipantVisitor {

    private UserId userId;

    @Override
    public void visit(ServiceParticipant participant) {
        throw new UnsupportedOperationException(
                "Sending messages not to users via telegram is not supported");
    }

    @Override
    public void visit(UnknownServiceParticipant participant) {
        throw new UnsupportedOperationException(
                "Sending messages not to users via telegram is not supported");
    }

    @Override
    public void visit(UserParticipant participant) {
        userId = participant.userId();
    }

    @Override
    public void visit(SelfParticipant participant) {
        throw new UnsupportedOperationException(
                "Sending messages not to users via telegram is not supported");
    }

    public @NotNull UserId getUserId() {
        if (userId == null) {
            throw new IllegalStateException("User not set");
        }
        return userId;
    }
}