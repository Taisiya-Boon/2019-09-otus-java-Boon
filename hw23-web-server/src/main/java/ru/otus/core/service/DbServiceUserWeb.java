package ru.otus.core.service;

import ru.otus.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DbServiceUserWeb extends DBServiceUser {
    Optional<User> getUserByLogin(String login);
    List<User> getAllUsers();
}
