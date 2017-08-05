package manager;

import db.entity.Role;
import db.entity.RoleCode;
import db.entity.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Eric on 3/25/2017.
 */
public interface UserManager {
    User createNewUser(String userName, String firstName, String lastName, String password,
                       Set<RoleCode> roleCodes, String accountCode, BigDecimal initialBalance);

    User saveNewPassword(String userName, String password);

    User saveUser(User user);

    User findByUserName(String userName);
}
