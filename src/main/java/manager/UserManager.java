package manager;

import db.dao.RoleRepository;
import db.dao.UserRepository;
import db.entity.Role;
import db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.auth.PasswordTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 3/19/2017.
 */
@Component
public class UserManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordTool passwordTool;

    // TODO Testing only
    public void createDefaultUser() {
        User defaultUser = new User();
        defaultUser.setFirstName("userfirstname");
        defaultUser.setLastName("userlastname");
        defaultUser.setUserName("user");
        Role role = roleRepository.findByRoleCode(RoleCode.USER);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        defaultUser.setRoles(roles);

        String salt = passwordTool.generateSalt(32);
        defaultUser.setSalt(salt);

        String passwordHash = passwordTool.hashPassword("user", salt);
        defaultUser.setPasswordHash(passwordHash);

        userRepository.save(defaultUser);
    }

    // TODO Testing only
    public void createAdminUser() {
        User adminUser = new User();
        adminUser.setFirstName("adminfirstname");
        adminUser.setLastName("adminlastname");
        adminUser.setUserName("admin");
        Role role = roleRepository.findByRoleCode(RoleCode.ADMIN);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        adminUser.setRoles(roles);
        String salt = passwordTool.generateSalt(32);
        adminUser.setSalt(salt);
        String passwordHash = passwordTool.hashPassword("admin", salt);
        adminUser.setPasswordHash(passwordHash);

        userRepository.save(adminUser);
    }

}
