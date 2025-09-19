package com.plux.distribution.infrastructure.persistence.entity.message.participant;

import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.participant.SelfParticipant;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SELF")
public class SelfParticipantEntity extends ParticipantEntity {
    public static SelfParticipantEntity fromModel(@SuppressWarnings("unused") SelfParticipant model) {
        return new SelfParticipantEntity();
    }

    @Override
    public Participant toModel() {
        return new SelfParticipant();
    }
}
