package com.plux.distribution.infrastructure.persistence.entity.message.participant;

import com.plux.distribution.domain.message.participant.Participant;
import com.plux.distribution.domain.message.participant.ParticipantVisitor;
import com.plux.distribution.domain.message.participant.SelfParticipant;
import com.plux.distribution.domain.message.participant.ServiceParticipant;
import com.plux.distribution.domain.message.participant.UnknownServiceParticipant;
import com.plux.distribution.domain.message.participant.ChatParticipant;
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
    private Long id;

    public abstract Participant toModel();

    public static ParticipantEntity fromModel(Participant participant) {
        return participant.accept(new ParticipantVisitor<>() {
            @Override
            public ParticipantEntity visit(ServiceParticipant participant) {
                return ServiceParticipantEntity.fromModel(participant);
            }

            @Override
            public ParticipantEntity visit(UnknownServiceParticipant participant) {
                return UnkServiceParticipantEntity.fromModel(participant);
            }

            @Override
            public ParticipantEntity visit(ChatParticipant participant) {
                return ChatParticipantEntity.fromModel(participant);
            }

            @Override
            public ParticipantEntity visit(SelfParticipant participant) {
                return SelfParticipantEntity.fromModel(participant);
            }
        });
    }
}
