package com.plux.distribution.infrastructure.persistence.config;

import jakarta.persistence.Entity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.Set;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

public class HibernateConfig {

    private final SessionFactory sessionFactory;

    public HibernateConfig(String dbUrl, String dbUser, String dbPassword) {
        runMigrations(dbUrl, dbUser, dbPassword);

        try {
            Configuration configuration = getConfiguration(dbUrl, dbUser, dbPassword);

            Reflections reflections = new Reflections("com.plux.distribution.infrastructure.persistence.entity");
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
        settings.put("hibernate.show_sql", "false");
        settings.put("hibernate.format_sql", "true");
        settings.put("hibernate.hbm2ddl.auto", "validate");
        settings.put("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl");

        Configuration configuration = new Configuration();
        configuration.setProperties(settings);
        return configuration;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private void runMigrations(String url, String user, String password) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update();
        } catch (Exception e) {
            throw new RuntimeException("Failed to run Liquibase migrations", e);
        }
    }
}
