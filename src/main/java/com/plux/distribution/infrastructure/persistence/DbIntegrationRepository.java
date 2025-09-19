package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.integration.application.command.IntegrationCommand;
import com.plux.distribution.core.integration.application.dto.Integration;
import com.plux.distribution.core.integration.application.dto.ServiceToken;
import com.plux.distribution.core.integration.application.dto.ServiceWebhook;
import com.plux.distribution.core.integration.application.exception.InvalidToken;
import com.plux.distribution.core.integration.application.port.out.IntegrationRepositoryPort;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.infrastructure.persistence.entity.integration.IntegrationEntity;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

public class DbIntegrationRepository implements IntegrationRepositoryPort {

    private final @NotNull SessionFactory sessionFactory;

    public DbIntegrationRepository(@NotNull SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull ServiceId create(@NotNull ServiceToken serviceToken, @NotNull IntegrationCommand command) {
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

    @Override
    public @NotNull List<Integration> getAll() {
        try (var session = sessionFactory.openSession()) {
            var query = session.createQuery("from IntegrationEntity", IntegrationEntity.class);

            return query
                    .stream()
                    .map(entity ->
                            new Integration(
                                    new ServiceId(entity.getId()),
                                    new ServiceWebhook(entity.getWebhookUrl())
                            ))
                    .toList();
        }
    }

    @Override
    public void delete(ServiceId id) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var entity = session.find(IntegrationEntity.class, id.value());
            session.remove(entity);

            transaction.commit();
        }
    }

    @Override
    public void put(@NotNull ServiceId id, @NotNull IntegrationCommand command) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var entity = session.find(IntegrationEntity.class, id.value());

            entity.setWebhookUrl(command.webhook());

            transaction.commit();
        }
    }
}
