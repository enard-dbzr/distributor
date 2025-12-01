package com.plux.distribution.infrastructure.persistence.entity.interaction.participant;

import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.Participant.ServiceParticipant;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SERVICE")
public class ServiceParticipantEntity extends ParticipantEntity {

    private Long serviceId;

    public ServiceParticipantEntity(Long serviceId) {
        this.serviceId = serviceId;
    }

    public ServiceParticipantEntity() {

    }

    @Override
    public Participant toModel() {
        return new ServiceParticipant(new ServiceId(serviceId));
    }
}
