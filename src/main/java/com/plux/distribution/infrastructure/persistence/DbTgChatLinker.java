package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.infrastructure.persistence.entity.specific.telegram.TgChatLinkEntity;
import com.plux.distribution.infrastructure.telegram.port.TgChatLinker;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DbTgChatLinker implements TgChatLinker {

    private final SessionFactory sessionFactory;

    public DbTgChatLinker(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public @Nullable ChatId getChatId(@NotNull Long tgChatId) {
        try (var session = sessionFactory.openSession()) {
            var query = session.createQuery(
                    "from TgChatLinkEntity where tgChatId = :tgId",
                    TgChatLinkEntity.class
            );
            query.setParameter("tgId", tgChatId);
            var entity = Optional.ofNullable(query.uniqueResult());

            return entity.map(e -> new ChatId(e.getChatId())).orElse(null);
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
