package manager;

import db.dao.AccountRepository;
import db.dao.RoleRepository;
import db.dao.UserRepository;
import db.entity.Account;
import db.entity.Role;
import db.entity.RoleCode;
import db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.auth.PasswordTool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Eric on 3/19/2017.
 */
@Component
public class UserManagerImpl implements UserManager {

    private static final int DEFAULT_SALT_LENGTH = 32;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordTool passwordTool;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public User createNewUser(String userName, String firstName, String lastName, String password, Set<RoleCode> roleCodes) {
        User user = new User();
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(new ArrayList<>());

        for (RoleCode roleCode : roleCodes) {
            Role role = roleRepository.findByRoleCode(roleCode);
            user.getRoles().add(role);
        }
        String salt = passwordTool.generateSalt(DEFAULT_SALT_LENGTH);
        user.setSalt(salt);
        String passwordHash = passwordTool.hashPassword(password, salt);
        user.setPasswordHash(passwordHash);
        userRepository.save(user);

        Account account = new Account();
        account.setOwner(user);
        account.setAccountName(userName + " Account");
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);

        user.setAccount(account);

        return user;
    }

    @Override
    public User saveNewPassword(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        String salt = passwordTool.generateSalt(DEFAULT_SALT_LENGTH);
        user.setSalt(salt);
        String passwordHash = passwordTool.hashPassword(password, salt);
        user.setPasswordHash(passwordHash);
        userRepository.save(user);
        return user;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
