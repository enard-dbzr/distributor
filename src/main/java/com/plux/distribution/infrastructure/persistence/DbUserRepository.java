package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.user.application.command.UserCommand;
import com.plux.distribution.core.user.application.port.out.UserRepositoryPort;
import com.plux.distribution.core.user.domain.User;
import com.plux.distribution.core.user.domain.UserId;
import com.plux.distribution.infrastructure.persistence.entity.user.UserEntity;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

public class DbUserRepository implements UserRepositoryPort {

    private final @NotNull SessionFactory sessionFactory;

    public DbUserRepository(@NotNull SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public @NotNull User create(@NotNull UserCommand createUserCommand) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = UserEntity.create(createUserCommand.userInfo());
            session.persist(entity);

            transaction.commit();

            return entity.toModel();
        }
    }

    @Override
    public void update(@NotNull User user) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = session.find(UserEntity.class, user.getId().value());
            entity.setUserInfo(user.getUserInfo());

            transaction.commit();
        }
    }

    @Override
    public @NotNull User get(@NotNull UserId id) {
        try (var session = sessionFactory.openSession()) {
            return session.find(UserEntity.class, id.value()).toModel();
        }
    }
}
