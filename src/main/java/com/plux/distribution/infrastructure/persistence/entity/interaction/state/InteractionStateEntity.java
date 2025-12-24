package com.plux.distribution.infrastructure.persistence.entity.interaction.state;

import com.plux.distribution.core.interaction.domain.InteractionState;
import com.plux.distribution.core.interaction.domain.InteractionState.Pending;
import com.plux.distribution.core.interaction.domain.InteractionState.Transferred;
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
@Table(name = "interaction_states")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class InteractionStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused")
    private Long id;

    public static InteractionStateEntity fromModel(InteractionState state) {
        return switch (state) {
            case Pending _ -> new PendingStateEntity();
            case Transferred s -> new TransferredStateEntity(s.transferTime());
        };
    }

    public abstract InteractionState toModel();
}
