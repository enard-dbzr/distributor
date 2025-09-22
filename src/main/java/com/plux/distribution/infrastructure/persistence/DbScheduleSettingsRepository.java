package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.session.application.port.out.ScheduleSettingsRepository;
import com.plux.distribution.core.session.domain.ScheduleSettings;
import com.plux.distribution.infrastructure.persistence.entity.session.ScheduleSettingsEntity;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

public class DbScheduleSettingsRepository implements ScheduleSettingsRepository {

    private final SessionFactory sessionFactory;

    public DbScheduleSettingsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void set(@NotNull ChatId chatId, @NotNull ScheduleSettings settings) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = ScheduleSettingsEntity.of(chatId, settings);
            session.merge(entity);

            transaction.commit();
        }
    }

    @Override
    public Optional<ScheduleSettings> get(@NotNull ChatId chatId) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(ScheduleSettingsEntity.class, chatId.value());

            return Optional.ofNullable(entity).map(ScheduleSettingsEntity::toModel);
        }
    }
}
