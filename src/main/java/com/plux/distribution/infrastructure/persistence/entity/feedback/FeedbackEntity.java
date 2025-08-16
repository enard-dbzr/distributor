package com.plux.distribution.infrastructure.persistence.entity.feedback;

import com.plux.distribution.domain.feedback.Feedback;
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

@Entity
@Table(name = "feedbacks")
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date actionTime;

    private Long userId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "payload_id")
    private FeedbackPayloadEntity payload;

    public static FeedbackEntity fromModel(Feedback model) {
        var entity = new FeedbackEntity();

        entity.actionTime = model.actionTime();
        entity.userId = model.userId().value();
        entity.payload = FeedbackPayloadEntity.fromModel(model.payload());

        return entity;
    }

    public Long getId() {
        return id;
    }
}
