package com.plux.distribution.infrastructure.persistence.entity.feedback;

import com.plux.distribution.application.dto.feedback.CreateFeedbackCommand;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.feedback.FeedbackId;
import com.plux.distribution.infrastructure.persistence.entity.feedback.payload.FeedbackPayloadEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "feedbacks")
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date actionTime;

    private Long chatId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "payload_id")
    private FeedbackPayloadEntity payload;

    public FeedbackEntity(@NotNull CreateFeedbackCommand command) {
        actionTime = command.actionTime();
        chatId = command.chatId().value();
        payload = FeedbackPayloadEntity.fromModel(command.payload());
    }

    public FeedbackEntity() {

    }

    public @NotNull Feedback toModel() {
        return new Feedback(
                new FeedbackId(id),
                actionTime,
                new ChatId(chatId),
                payload.toModel()
        );
    }

    public Long getId() {
        return id;
    }
}
