package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.MyCache;
import ru.otus.core.dao.UserDaoWeb;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbServiceUserWebImpl extends DbServiceUserImpl implements DbServiceUserWeb {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserWebImpl.class);

    private final UserDaoWeb userDao;
    private final MyCache<String, User> myCache = new MyCache<>();

    public DbServiceUserWebImpl(UserDaoWeb userDao) {
        super(userDao);
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        myCache.put(user.getLogin() + "/" + user.getId(), user);
        return super.saveUser(user);
    }

    @Override
    public Optional<User> getUser(long id) {
        for (String key : myCache.getKeys()) {
            if (key.endsWith("/" + myCache.get(key).getId())) {
                return Optional.ofNullable(myCache.get(key));
            }
        }
        return super.getUser(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        for (String key : myCache.getKeys()) {
            if (key.startsWith(myCache.get(key).getLogin() + "/")) {
                return Optional.ofNullable(myCache.get(key));
            }
        }
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findByLogin(login);

                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                List<User> allUsers = userDao.getAllUsers();

                logger.info("get all users");
                return allUsers;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return new ArrayList<>();
        }
    }
}
