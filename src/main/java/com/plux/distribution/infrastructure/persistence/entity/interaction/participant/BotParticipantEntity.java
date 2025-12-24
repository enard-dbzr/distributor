package com.plux.distribution.infrastructure.persistence.entity.interaction.participant;

import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BOT")
public class BotParticipantEntity extends ParticipantEntity {

    @Override
    public Participant toModel() {
        return new BotParticipant();
    }
}
