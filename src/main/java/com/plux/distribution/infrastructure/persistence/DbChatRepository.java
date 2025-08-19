package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.chat.CreateChatPort;
import com.plux.distribution.domain.chat.Chat;
import com.plux.distribution.domain.chat.ChatId;
import com.plux.distribution.infrastructure.persistence.entity.chat.ChatEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

public class DbChatRepository implements CreateChatPort {
    private final SessionFactory sessionFactory;

    public DbChatRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull ChatId create(@NotNull Chat chat) {
        try (Session session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var entity = ChatEntity.fromModel(chat);
            session.persist(entity);

            transaction.commit();

            System.out.printf("New chat id: %s%n", entity.getId());

            return new ChatId(entity.getId());
        }
    }
}
