package ru.otus.core.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.User;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.*;

public class UserDaoWebImpl extends UserDaoHibernate implements UserDaoWeb {
    private static Logger logger = LoggerFactory.getLogger(UserDaoWebImpl.class);

    private final SessionManagerHibernate sessionManager;

    public UserDaoWebImpl(SessionManagerHibernate sessionManager) {
        super(sessionManager);
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            User user = currentSession.getHibernateSession().createQuery(
                    "select u from User u where u.login = :paramLogin", User.class)
                    .setParameter("paramLogin", login)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        List<User> users = new ArrayList<>();
        try {
            users = currentSession.getHibernateSession().createQuery(
                    "select u from User u", User.class)
                    .getResultList();
            return users;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return users;
    }
}
