package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.integration.application.port.out.ServiceSendingRepositoryPort;
import com.plux.distribution.core.integration.domain.ServiceId;
import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.infrastructure.persistence.entity.integration.ServiceSendingEntity;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

public class DbServiceSendingRepository implements ServiceSendingRepositoryPort {

    private final @NotNull SessionFactory sessionFactory;

    public DbServiceSendingRepository(@NotNull SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(@NotNull InteractionId sending, @NotNull ServiceId serviceId) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var entity = new ServiceSendingEntity(sending.value(), serviceId.value());

            session.persist(entity);

            transaction.commit();
        }
    }

    @Override
    public @NotNull ServiceId getBySending(@NotNull InteractionId interactionId) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(ServiceSendingEntity.class, interactionId.value());

            return new ServiceId(entity.getServiceId());
        }
    }
}
