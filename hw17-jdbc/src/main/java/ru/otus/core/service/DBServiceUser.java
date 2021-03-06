package ru.otus.core.service;

import ru.otus.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

  long saveUser(User user);

  void updateUser(User user);

  Optional<User> getUser(long id);

}
