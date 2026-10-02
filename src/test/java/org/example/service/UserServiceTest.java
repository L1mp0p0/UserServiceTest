package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.dao.UserDao;
import org.example.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao);
    }

    @Test
    void shouldCreateUser() {

        User user = new User(
                "Alex",
                "alex@mail.ru",
                20
        );

        userService.create(user);

        verify(userDao).save(user);
    }

    @Test
    void shouldGetUserById() {

        User user = new User(
                "Alex",
                "alex@mail.ru",
                20
        );

        when(userDao.findById(1L))
                .thenReturn(user);

        User result = userService.getById(1L);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userDao).findById(1L);
    }

    @Test
    void shouldGetAllUsers() {

        User user1 = new User(
                "Alex",
                "alex@mail.ru",
                20
        );

        User user2 = new User(
                "Ivan",
                "ivan@mail.ru",
                25
        );

        List<User> users = List.of(user1, user2);

        when(userDao.findAll())
                .thenReturn(users);

        List<User> result = userService.getAll();

        assertEquals(2, result.size());
        assertEquals(users, result);

        verify(userDao).findAll();
    }

    @Test
    void shouldUpdateUser() {

        User user = new User(
                "Alex",
                "alex@mail.ru",
                20
        );

        userService.update(user);

        verify(userDao).update(user);
    }

    @Test
    void shouldDeleteUser() {

        userService.delete(1L);

        verify(userDao).delete(1L);
    }
}