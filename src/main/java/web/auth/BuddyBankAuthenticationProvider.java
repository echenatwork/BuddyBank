package web.auth;

import db.dao.UserRepository;
import db.entity.User;
import manager.UserManager;
import manager.UserManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 3/12/2017.
 */
public class BuddyBankAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordTool passwordTool;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (StringUtils.isEmpty(name) || password == null) {
            return null;
        }

        User user = userRepository.findByUserName(name);

        if (user == null) {
            return null;
        }

        if (passwordTool.hashPassword(password, user.getSalt()).equals(user.getPasswordHash())) {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.addAll(user.getRoles());

            // Let's store the user object in the session for now
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().setAttribute("user", user);

            // Should I pass the user object or the username here?
            return new UsernamePasswordAuthenticationToken(user.getUserName(), password, authorities);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
