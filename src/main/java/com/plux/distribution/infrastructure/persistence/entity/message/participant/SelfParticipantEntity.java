package com.plux.distribution.infrastructure.persistence.entity.message.participant;

import com.plux.distribution.domain.message.participant.Participant;
import com.plux.distribution.domain.message.participant.SelfParticipant;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SELF")
public class SelfParticipantEntity extends ParticipantEntity {
    public static SelfParticipantEntity fromModel(SelfParticipant model) {
        return new SelfParticipantEntity();
    }

    @Override
    public Participant toModel() {
        return new SelfParticipant();
    }
}
