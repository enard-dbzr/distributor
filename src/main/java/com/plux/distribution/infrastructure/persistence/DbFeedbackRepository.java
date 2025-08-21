package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.dto.feedback.CreateFeedbackCommand;
import com.plux.distribution.application.port.out.feedback.CreateFeedbackPort;
import com.plux.distribution.domain.feedback.Feedback;
import com.plux.distribution.infrastructure.persistence.entity.feedback.FeedbackEntity;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

public class DbFeedbackRepository implements CreateFeedbackPort {
    private final SessionFactory sessionFactory;

    public DbFeedbackRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull Feedback create(@NotNull CreateFeedbackCommand command) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var entity = new FeedbackEntity(command);

            session.persist(entity);

            transaction.commit();

            return entity.toModel();
        }
    }
}
