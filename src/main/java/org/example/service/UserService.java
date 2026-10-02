package org.example.service;

import org.example.entity.User;

import java.util.List;

public interface UserService {

    void create(User user);

    User getById(Long id);

    List<User> getAll();

    void update(User user);

    void delete(Long id);
}