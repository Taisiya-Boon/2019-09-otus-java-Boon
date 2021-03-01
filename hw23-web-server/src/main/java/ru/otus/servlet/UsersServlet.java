package ru.otus.servlet;

import org.eclipse.jetty.server.session.Session;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUserWeb;
import ru.otus.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersServlet extends HttpServlet {
    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USERS = "Users";
    private static final String TEMPLATE_ATTR_USER_AUTH = "User_admin";

    private final DbServiceUserWeb dbServiceUserWeb;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor, DbServiceUserWeb dbServiceUserWeb) {
        this.templateProcessor = templateProcessor;
        this.dbServiceUserWeb = dbServiceUserWeb;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<User> users = dbServiceUserWeb.getAllUsers();
        paramsMap.put(TEMPLATE_ATTR_USERS, users);

//        Session session = (Session) req.getSession();
//        Long id = Long.valueOf(session.getExtendedId());
        boolean adminFlag = false;
//        if (dbServiceUserWeb.getUser(id).isPresent()) {
//            if (dbServiceUserWeb.getUser(id).get().getAdminFlag()) {
//                adminFlag = true;
//            }
//        }
        paramsMap.put(TEMPLATE_ATTR_USER_AUTH, adminFlag);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }
}
