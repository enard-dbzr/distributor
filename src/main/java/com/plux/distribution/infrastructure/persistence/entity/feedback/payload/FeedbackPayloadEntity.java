package com.plux.distribution.infrastructure.persistence.entity.feedback.payload;

import com.plux.distribution.core.feedback.domain.payload.ButtonPayload;
import com.plux.distribution.core.feedback.domain.payload.FeedbackPayload;
import com.plux.distribution.core.feedback.domain.payload.FeedbackPayloadVisitor;
import com.plux.distribution.core.feedback.domain.payload.MessagePayload;
import com.plux.distribution.core.feedback.domain.payload.ReplyPayload;
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
    @SuppressWarnings("unused")
    private Long id;

    public static FeedbackPayloadEntity fromModel(FeedbackPayload model) {
        return model.accept(new FeedbackPayloadVisitor<>() {
            @Override
            public FeedbackPayloadEntity visit(@NotNull ButtonPayload entity) {
                return ButtonPayloadEntity.fromModel(entity);
            }

            @Override
            public FeedbackPayloadEntity visit(@NotNull MessagePayload entity) {
                return MessagePayloadEntity.fromModel(entity);
            }

            @Override
            public FeedbackPayloadEntity visit(@NotNull ReplyPayload entity) {
                return ReplyPayloadEntity.fromModel(entity);
            }
        });
    }
}
