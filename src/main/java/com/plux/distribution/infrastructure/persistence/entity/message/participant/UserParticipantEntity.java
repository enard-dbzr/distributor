package com.plux.distribution.infrastructure.persistence.entity.message.participant;

import com.plux.distribution.domain.message.participant.Participant;
import com.plux.distribution.domain.message.participant.UserParticipant;
import com.plux.distribution.domain.user.UserId;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("USER")
public class UserParticipantEntity extends ParticipantEntity {
    private Long userId;

    @Override
    public Participant toModel() {
        return new UserParticipant(new UserId(userId));
    }

    public static   UserParticipantEntity fromModel(UserParticipant model) {
        var entity = new UserParticipantEntity();

        entity.userId = model.userId().value();

        return entity;
    }
}
