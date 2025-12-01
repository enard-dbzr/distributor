package com.plux.distribution.infrastructure.persistence.entity.interaction.participant;

import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ServiceParticipant;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "participants")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class ParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused")
    private Long id;

    public static ParticipantEntity fromModel(Participant participant) {
        return switch (participant) {
            case ChatParticipant p -> new ChatParticipantEntity(p.chatId().value());
            case ServiceParticipant p -> new ServiceParticipantEntity(p.serviceId().value());
            case BotParticipant _ -> new BotParticipantEntity();
        };
    }

    public abstract Participant toModel();
}
