package com.plux.distribution.infrastructure.persistence.entity.message.participant;

import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.participant.UnknownServiceParticipant;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("UNK_SERVICE")
public class UnkServiceParticipantEntity extends ParticipantEntity {

    @Override
    public Participant toModel() {
        return new UnknownServiceParticipant();
    }

    public static UnkServiceParticipantEntity fromModel(@SuppressWarnings("unused") UnknownServiceParticipant model) {
        return new UnkServiceParticipantEntity();
    }
}
