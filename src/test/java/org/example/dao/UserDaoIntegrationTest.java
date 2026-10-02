package org.example.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.example.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
class UserDaoIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:18")
                    .withDatabaseName("user_service")
                    .withUsername("postgres")
                    .withPassword("123");

    private static SessionFactory sessionFactory;

    private UserDao userDao;

    @BeforeAll
    static void setUpDatabase() {

        Configuration configuration = new Configuration();

        configuration.setProperty(
                "hibernate.connection.driver_class",
                "org.postgresql.Driver"
        );

        configuration.setProperty(
                "hibernate.connection.url",
                postgres.getJdbcUrl()
        );

        configuration.setProperty(
                "hibernate.connection.username",
                postgres.getUsername()
        );

        configuration.setProperty(
                "hibernate.connection.password",
                postgres.getPassword()
        );

        configuration.setProperty(
                "hibernate.dialect",
                "org.hibernate.dialect.PostgreSQLDialect"
        );

        configuration.setProperty(
                "hibernate.hbm2ddl.auto",
                "create"
        );

        configuration.setProperty(
                "hibernate.show_sql",
                "true"
        );

        configuration.addAnnotatedClass(User.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    @BeforeEach
    void cleanDatabase() {

        userDao = new UserDaoImpl(sessionFactory);

        try (var session = sessionFactory.openSession()) {

            var transaction = session.beginTransaction();

            session.createMutationQuery("DELETE FROM User")
                    .executeUpdate();

            transaction.commit();
        }
    }

    @AfterAll
    static void tearDown() {

        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void shouldSaveUser() {

        User user = new User(
                "Alex",
                "alex@mail.ru",
                20
        );

        userDao.save(user);

        assertNotNull(user.getId());

        User savedUser = userDao.findById(user.getId());

        assertNotNull(savedUser);
        assertEquals("Alex", savedUser.getName());
        assertEquals("alex@mail.ru", savedUser.getEmail());
        assertEquals(20, savedUser.getAge());
    }

    @Test
    void shouldFindUserById() {

        User user = new User(
                "Ivan",
                "ivan@mail.ru",
                25
        );

        userDao.save(user);

        User result = userDao.findById(user.getId());

        assertNotNull(result);
        assertEquals("Ivan", result.getName());
        assertEquals("ivan@mail.ru", result.getEmail());
    }

    @Test
    void shouldFindAllUsers() {

        userDao.save(
                new User(
                        "Alex",
                        "alex@mail.ru",
                        20
                )
        );

        userDao.save(
                new User(
                        "Ivan",
                        "ivan@mail.ru",
                        25
                )
        );

        List<User> users = userDao.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void shouldUpdateUser() {

        User user = new User(
                "Alex",
                "alex@mail.ru",
                20
        );

        userDao.save(user);

        user.setName("Alexander");
        user.setAge(21);

        userDao.update(user);

        User updatedUser = userDao.findById(user.getId());

        assertNotNull(updatedUser);
        assertEquals("Alexander", updatedUser.getName());
        assertEquals(21, updatedUser.getAge());
    }

    @Test
    void shouldDeleteUser() {

        User user = new User(
                "Alex",
                "alex@mail.ru",
                20
        );

        userDao.save(user);

        Long id = user.getId();

        userDao.delete(id);

        User deletedUser = userDao.findById(id);

        assertNull(deletedUser);
    }
}