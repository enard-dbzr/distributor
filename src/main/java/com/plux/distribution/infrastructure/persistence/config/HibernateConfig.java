package com.plux.distribution.infrastructure.persistence.config;

import jakarta.persistence.Entity;
import java.util.Properties;
import java.util.Set;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

public class HibernateConfig {

    private SessionFactory sessionFactory;

    public HibernateConfig(String dbUrl, String dbUser, String dbPassword) {
        try {
            Configuration configuration = getConfiguration(dbUrl, dbUser, dbPassword);

            Reflections reflections = new Reflections(
                    "com.plux.distribution.infrastructure.persistence.entity");
            Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);

            for (Class<?> entityClass : entities) {
                configuration.addAnnotatedClass(entityClass);
            }

            StandardServiceRegistryBuilder registryBuilder =
                    new StandardServiceRegistryBuilder().applySettings(
                            configuration.getProperties());

            sessionFactory = configuration.buildSessionFactory(registryBuilder.build());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Hibernate", e);
        }
    }

    private static @NotNull Configuration getConfiguration(String dbUrl, String dbUser,
            String dbPassword) {
        Properties settings = new Properties();

        settings.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        settings.put("hibernate.connection.url", dbUrl);
        settings.put("hibernate.connection.username", dbUser);
        settings.put("hibernate.connection.password", dbPassword);
        settings.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        settings.put("hibernate.show_sql", "true");
        settings.put("hibernate.format_sql", "true");
        settings.put("hibernate.hbm2ddl.auto", "update");

        Configuration configuration = new Configuration();
        configuration.setProperties(settings);
        return configuration;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
