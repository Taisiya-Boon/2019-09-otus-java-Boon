package ru.otus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.service.DbServiceUserWeb;

public class UserAuthServiceImpl implements UserAuthService {
    private static Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);

    private DbServiceUserWeb dbService;

    public UserAuthServiceImpl(DbServiceUserWeb dbService) {
        this.dbService = dbService;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbService.getUserByLogin(login)
                    .map(user -> user.getPassword().equals(password))
                    .orElse(false);
    }
}
