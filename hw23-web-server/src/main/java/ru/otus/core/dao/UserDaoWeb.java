package ru.otus.core.dao;

import ru.otus.core.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDaoWeb extends UserDao {
    Optional<User> findByLogin(String login);
    List<User> getAllUsers();
}
