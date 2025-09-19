package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.core.user.application.command.CreateUserCommand;
import com.plux.distribution.core.user.application.port.out.CreateUserPort;
import com.plux.distribution.core.user.domain.User;
import com.plux.distribution.infrastructure.persistence.entity.user.UserEntity;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;

public class DbUserRepository implements CreateUserPort {
    private final @NotNull SessionFactory sessionFactory;

    public DbUserRepository(@NotNull SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public User create(CreateUserCommand createUserCommand) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = UserEntity.create(createUserCommand.userInfo());
            session.persist(entity);

            transaction.commit();

            return entity.toModel();
        }
    }
}
