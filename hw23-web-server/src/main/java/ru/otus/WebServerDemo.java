package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.SessionFactory;
import ru.otus.core.dao.UserDaoWeb;
import ru.otus.core.dao.UserDaoWebImpl;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUserWeb;
import ru.otus.core.service.DbServiceUserWebImpl;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UserWebServer;
import ru.otus.server.UserWebServerImpl;
import ru.otus.services.*;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/

public class WebServerDemo {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDaoWeb userDao = new UserDaoWebImpl(sessionManager);
        DbServiceUserWeb dbServiceUserWeb = new DbServiceUserWebImpl(userDao);
        addStartUsers(dbServiceUserWeb);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        UserAuthService userAuthService = new UserAuthServiceImpl(dbServiceUserWeb);
        LoginService loginService = new LoginServiceImpl(dbServiceUserWeb);

        UserWebServer usersWebServer = new UserWebServerImpl(WEB_SERVER_PORT,
                    dbServiceUserWeb, userAuthService, gson, templateProcessor, loginService);

        usersWebServer.start();
        usersWebServer.join();
    }

    private static void addStartUsers(DbServiceUserWeb dbServiceUserWeb) {
        dbServiceUserWeb.saveUser(new User(0L, "Тася", "user1", "11111", true));
        dbServiceUserWeb.saveUser(new User(0L, "Петя", "user2", "11112", false));
        dbServiceUserWeb.saveUser(new User(0L, "Оля", "user3", "11113", false));
        dbServiceUserWeb.saveUser(new User(0L, "Вася", "user4", "11114", false));
        dbServiceUserWeb.saveUser(new User(0L, "Катя", "user5", "11115", false));
        dbServiceUserWeb.saveUser(new User(0L, "Вика", "user6", "11116", false));
    }

}
