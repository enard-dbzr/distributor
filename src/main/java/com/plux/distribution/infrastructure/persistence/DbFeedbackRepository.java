package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.feedback.application.command.CreateFeedbackCommand;
import com.plux.distribution.core.feedback.application.dto.Feedback;
import com.plux.distribution.core.feedback.application.port.out.CreateFeedbackPort;
import com.plux.distribution.core.feedback.domain.FeedbackId;
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

            return new Feedback(
                    new FeedbackId(entity.getId()),
                    command.actionTime(),
                    command.chatId(),
                    command.payload()
            );
        }
    }
}
