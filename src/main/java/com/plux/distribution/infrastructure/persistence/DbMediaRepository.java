package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.mediastorage.application.exception.DuplicateMediaIdException;
import com.plux.distribution.core.mediastorage.application.port.out.MediaRepositoryPort;
import com.plux.distribution.core.mediastorage.domain.Media;
import com.plux.distribution.core.mediastorage.domain.MediaId;
import com.plux.distribution.infrastructure.persistence.entity.mediastorage.MediaEntity;
import jakarta.persistence.PersistenceException;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;

public class DbMediaRepository implements MediaRepositoryPort {

    private final @NotNull SessionFactory sessionFactory;

    public DbMediaRepository(@NotNull SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(@NotNull Media media) throws DuplicateMediaIdException {
        try (Session session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                MediaEntity entity = new MediaEntity(media);
                session.persist(entity);
                transaction.commit();
            } catch (PersistenceException e) {
                transaction.rollback();

                Throwable rootCause = e.getCause();
                if (rootCause instanceof ConstraintViolationException) {
                    throw new DuplicateMediaIdException(media.id());
                }

                throw e;
            }
        }
    }

    @Override
    public @NotNull Optional<Media> findById(@NotNull MediaId id) {
        try (var session = sessionFactory.openSession()) {
            MediaEntity entity = session.find(MediaEntity.class, id.value());
            return Optional.ofNullable(entity).map(MediaEntity::toModel);
        }
    }

    @Override
    public void delete(@NotNull MediaId id) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            MediaEntity entityToDelete = session.find(MediaEntity.class, id.value());
            if (entityToDelete != null) {
                session.remove(entityToDelete);
            }
            transaction.commit();
        }
    }
}
