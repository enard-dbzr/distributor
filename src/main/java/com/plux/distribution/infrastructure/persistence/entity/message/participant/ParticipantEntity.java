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
import java.util.concurrent.atomic.AtomicReference;

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
        var holder = new AtomicReference<ParticipantEntity>();

        participant.accept(new ParticipantVisitor() {
            @Override
            public void visit(ServiceParticipant participant) {
                holder.set(ServiceParticipantEntity.fromModel(participant));
            }

            @Override
            public void visit(UnknownServiceParticipant participant) {
                holder.set(UnkServiceParticipantEntity.fromModel(participant));
            }

            @Override
            public void visit(ChatParticipant participant) {
                holder.set(ChatParticipantEntity.fromModel(participant));
            }

            @Override
            public void visit(SelfParticipant participant) {
                holder.set(SelfParticipantEntity.fromModel(participant));
            }
        });

        return holder.get();
    }
}
