package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.chat.CreateChatPort;
import com.plux.distribution.application.port.out.chat.GetChatPort;
import com.plux.distribution.application.port.out.chat.UpdateChatPort;
import com.plux.distribution.domain.chat.Chat;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.infrastructure.persistence.entity.chat.ChatEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

public class DbChatRepository implements CreateChatPort, UpdateChatPort, GetChatPort {

    private final SessionFactory sessionFactory;

    public DbChatRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull Chat create() {
        try (Session session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var entity = new ChatEntity();
            session.persist(entity);

            transaction.commit();

            return entity.toModel();
        }
    }

    @Override
    public @NotNull Chat get(@NotNull ChatId id) {
        try (Session session = sessionFactory.openSession()) {
            var chat = session.find(ChatEntity.class, id.value());

            return chat.toModel();
        }
    }

    @Override
    public void update(@NotNull Chat chat) {
        try (Session session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var entity = new ChatEntity(chat);
            session.merge(entity);

            transaction.commit();
        }
    }
}
