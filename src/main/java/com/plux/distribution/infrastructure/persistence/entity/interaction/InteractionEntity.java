package com.plux.distribution.infrastructure.persistence.entity.interaction;

import com.plux.distribution.core.interaction.application.command.CreateInteractionCommand;
import com.plux.distribution.core.interaction.domain.Interaction;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.infrastructure.persistence.entity.interaction.content.InteractionContentEntity;
import com.plux.distribution.infrastructure.persistence.entity.interaction.participant.ParticipantEntity;
import com.plux.distribution.infrastructure.persistence.entity.interaction.state.InteractionStateEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "interactions")
public class InteractionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private ParticipantEntity sender;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_id")
    private ParticipantEntity recipient;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "content_id")
    private InteractionContentEntity content;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private InteractionStateEntity state;


    public InteractionEntity(CreateInteractionCommand command) {
        sender = ParticipantEntity.fromModel(command.sender());
        recipient = ParticipantEntity.fromModel(command.recipient());
        state = InteractionStateEntity.fromModel(command.state());
        content = InteractionContentEntity.fromModel(command.content());
    }

    public InteractionEntity() {

    }

    public Interaction toModel() {
        return new Interaction(
                new InteractionId(id),
                sender.toModel(),
                recipient.toModel(),
                state.toModel(),
                content.toModel()
        );
    }

    public InteractionStateEntity getState() {
        return state;
    }

    public void setState(
            InteractionStateEntity state) {
        this.state = state;
    }
}
