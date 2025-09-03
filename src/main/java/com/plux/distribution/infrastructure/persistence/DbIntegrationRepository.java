package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.dto.integration.CreateIntegrationCommand;
import com.plux.distribution.application.dto.integration.ServiceToken;
import com.plux.distribution.application.port.exception.InvalidToken;
import com.plux.distribution.application.port.out.integration.IntegrationRepositoryPort;
import com.plux.distribution.domain.service.ServiceId;
import com.plux.distribution.infrastructure.persistence.entity.integration.IntegrationEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

public class DbIntegrationRepository implements IntegrationRepositoryPort {

    private final @NotNull SessionFactory sessionFactory;

    public DbIntegrationRepository(@NotNull SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull ServiceId create(@NotNull ServiceToken serviceToken, @NotNull CreateIntegrationCommand command) {
        try (Session session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var entity = new IntegrationEntity(command.webhook(), serviceToken.token());
            session.persist(entity);

            transaction.commit();

            return new ServiceId(entity.getId());
        }
    }

    @Override
    public @NotNull ServiceId findId(@NotNull ServiceToken token) throws InvalidToken {
        try (var session = sessionFactory.openSession()) {
            var query = session.createQuery(
                    "from IntegrationEntity where token = :token",
                    IntegrationEntity.class
            );
            query.setParameter("token", token.token());

            var entity = query.uniqueResult();
            if (entity == null) {
                throw new InvalidToken("Service with specified token was not found");
            }

            return new ServiceId(entity.getId());
        }
    }

    @Override
    public @NotNull String getWebhook(@NotNull ServiceId id) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(IntegrationEntity.class, id.value());

            return entity.getWebhookUrl();
        }
    }
}
