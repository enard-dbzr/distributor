package com.plux.distribution.infrastructure.persistence.entity.message.participant;

import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.participant.ServiceParticipant;
import com.plux.distribution.core.integration.domain.ServiceId;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SERVICE")
public class ServiceParticipantEntity extends ParticipantEntity {
    private Long serviceId;

    public static ServiceParticipantEntity fromModel(ServiceParticipant model) {
        var entity = new ServiceParticipantEntity();

        entity.serviceId = model.serviceId().value();

        return entity;
    }

    @Override
    public Participant toModel() {
        return new ServiceParticipant(new ServiceId(serviceId));
    }
}
