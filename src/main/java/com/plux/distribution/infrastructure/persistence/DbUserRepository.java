package com.plux.distribution.infrastructure.persistence;

import com.plux.distribution.application.port.out.user.CreateUserPort;
import com.plux.distribution.domain.user.User;
import com.plux.distribution.domain.user.UserInfo;
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
    public User create(UserInfo userInfo) {
        try (var session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            var entity = UserEntity.create(userInfo);
            session.persist(entity);

            transaction.commit();

            return entity.toModel();
        }
    }
}
