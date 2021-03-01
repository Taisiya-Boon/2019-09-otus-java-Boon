package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.util.security.Password;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUserWeb;

import java.util.Optional;

@RequiredArgsConstructor
public class LoginServiceImpl extends AbstractLoginService {

    private final DbServiceUserWeb dbServiceUserWeb;

    @Override
    protected String[] loadRoleInfo(UserPrincipal userPrincipal) {
        if (dbServiceUserWeb.getUserByLogin(userPrincipal.getName()).isPresent()) {
               if (dbServiceUserWeb.getUserByLogin(userPrincipal.getName()).get().getAdminFlag()) {
                   return new String[]{"admin"};
               }
        }
        return new String[] {"user"};
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        System.out.println(String.format("InMemoryLoginService#loadUserInfo(%s)", login));
        Optional<User> dbUser = dbServiceUserWeb.getUserByLogin(login);
        return dbUser.map(u -> new UserPrincipal(u.getLogin(), new Password(u.getPassword()))).orElse(null);
    }
}
