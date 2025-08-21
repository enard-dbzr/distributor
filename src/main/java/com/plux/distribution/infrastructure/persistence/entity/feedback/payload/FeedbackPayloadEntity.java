package com.plux.distribution.infrastructure.persistence.entity.feedback.payload;

import com.plux.distribution.domain.feedback.payload.ButtonPayload;
import com.plux.distribution.domain.feedback.payload.FeedbackPayload;
import com.plux.distribution.domain.feedback.payload.FeedbackPayloadVisitor;
import com.plux.distribution.domain.feedback.payload.MessagePayload;
import com.plux.distribution.domain.feedback.payload.ReplyPayload;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "feedback_payloads")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class FeedbackPayloadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public static FeedbackPayloadEntity fromModel(FeedbackPayload model) {
        final FeedbackPayloadEntity[] payload = new FeedbackPayloadEntity[1];

        model.accept(new FeedbackPayloadVisitor() {
            @Override
            public void visit(@NotNull ButtonPayload entity) {
                payload[0] = ButtonPayloadEntity.fromModel(entity);
            }

            @Override
            public void visit(@NotNull MessagePayload entity) {
                payload[0] = MessagePayloadEntity.fromModel(entity);
            }

            @Override
            public void visit(@NotNull ReplyPayload entity) {
                payload[0] = ReplyPayloadEntity.fromModel(entity);
            }
        });

        return payload[0];
    }

    public abstract @NotNull FeedbackPayload toModel();

}
