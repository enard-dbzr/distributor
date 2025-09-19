package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.chat.application.port.out.CreateChatPort;
import com.plux.distribution.core.chat.application.port.out.GetChatPort;
import com.plux.distribution.core.chat.application.port.out.UpdateChatPort;
import com.plux.distribution.core.chat.domain.Chat;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.infrastructure.persistence.entity.chat.ChatEntity;
import java.util.List;
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
    public @NotNull List<ChatId> getAllChatIds() {
        try (Session session = sessionFactory.openSession()) {
            var ids = session.createQuery("select c.id from ChatEntity c", Long.class).getResultList();

            return ids.stream().map(ChatId::new).toList();
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
