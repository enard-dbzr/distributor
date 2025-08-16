package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.feedback.CreateFeedbackPort;
import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.domain.feedback.FeedbackId;
import com.plux.distribution.infrastructure.persistence.entity.feedback.FeedbackEntity;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

public class DbFeedbackRepository implements CreateFeedbackPort {
    private final SessionFactory sessionFactory;

    public DbFeedbackRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull FeedbackId create(@NotNull Feedback feedback) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var entity = FeedbackEntity.fromModel(feedback);

            session.persist(entity);

            transaction.commit();

            return new FeedbackId(entity.getId());
        }
    }
}
