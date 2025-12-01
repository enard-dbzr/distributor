package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.feedback.application.exception.ChatIdNotFound;
import com.plux.distribution.infrastructure.persistence.entity.specific.telegram.TgChatLinkEntity;
import com.plux.distribution.infrastructure.telegram.port.chat.GetChatIdByTgPort;
import com.plux.distribution.infrastructure.telegram.port.chat.GetTgChatIdPort;
import com.plux.distribution.infrastructure.telegram.port.chat.TgChatLinker;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

public class DbTgChatLinker implements GetTgChatIdPort, GetChatIdByTgPort, TgChatLinker {

    private final SessionFactory sessionFactory;

    public DbTgChatLinker(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @NotNull ChatId getChatId(@NotNull Long tgChatId) throws ChatIdNotFound {
        try (var session = sessionFactory.openSession()) {
            var query = session.createQuery(
                    "from TgChatLinkEntity where tgChatId = :tgId",
                    TgChatLinkEntity.class
            );
            query.setParameter("tgId", tgChatId);
            var entity = query.uniqueResult();

            if (entity == null) {
                throw new ChatIdNotFound("ChatId not found");
            }

            return new ChatId(entity.getChatId());
        }
    }

    @Override
    public @NotNull Long getTgChatId(@NotNull ChatId chatId) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(TgChatLinkEntity.class, chatId.value());

            return entity.getTgChatId();
        }
    }

    @Override
    public void link(@NotNull ChatId internal, @NotNull Long external) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = new TgChatLinkEntity(internal.value(), external);

            session.persist(entity);

            transaction.commit();
        }
    }
}
