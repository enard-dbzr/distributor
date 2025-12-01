package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.interaction.domain.InteractionId;
import com.plux.distribution.infrastructure.persistence.entity.specific.telegram.TgMessageLinkEntity;
import com.plux.distribution.infrastructure.telegram.port.message.GetMessageIdByTgPort;
import com.plux.distribution.infrastructure.telegram.port.message.GetTgMessageIdPort;
import com.plux.distribution.infrastructure.telegram.port.message.TgMessageGlobalId;
import com.plux.distribution.infrastructure.telegram.port.message.TgMessageLinker;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

public class DbTgMessageLinker implements TgMessageLinker, GetMessageIdByTgPort, GetTgMessageIdPort {

    private final @NotNull SessionFactory sessionFactory;

    public DbTgMessageLinker(@NotNull SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public @NotNull InteractionId getInteractionId(@NotNull TgMessageGlobalId tgGlobalId) {
        try (var session = sessionFactory.openSession()) {
            var query = session.createQuery(
                    "from TgMessageLinkEntity where tgMessageId = :mid and tgChatId = :cid",
                    TgMessageLinkEntity.class
            );
            query.setParameter("mid", tgGlobalId.messageId());
            query.setParameter("cid", tgGlobalId.chatId());

            var entity = query.uniqueResult();

            return new InteractionId(entity.getInteractionId());
        }
    }

    @Override
    public TgMessageGlobalId getTgMessageId(@NotNull InteractionId interactionId) {
        try (var session = sessionFactory.openSession()) {
            var entity = session.find(TgMessageLinkEntity.class, interactionId.value());
            if (entity == null) {
                return null;
            }
            return new TgMessageGlobalId(entity.getTgMessageId(), entity.getTgChatId());
        }
    }

    @Override
    public void link(@NotNull InteractionId internal, @NotNull TgMessageGlobalId external) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = new TgMessageLinkEntity(
                    internal.value(),
                    external.messageId(),
                    external.chatId()
            );

            session.persist(entity);

            transaction.commit();
        }
    }
}
