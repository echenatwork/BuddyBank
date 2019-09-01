package manager;

import db.dao.AccountRepository;
import db.dao.AccountTransactionRepository;
import db.dao.RoleRepository;
import db.dao.UserRepository;
import db.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import web.auth.PasswordTool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Eric on 3/19/2017.
 */
@Component
public class UserManagerImpl implements UserManager {

    private static final String INITIAL_ACCOUNT_TRANSACTION_DESCRIPTION = "Initial account transaction";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordTool passwordTool;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;

    @Override
    @Transactional
    public User createNewUser(String userName, String firstName, String lastName, String password,
                              Set<RoleCode> roleCodes, String accountCode, BigDecimal initialBalance) {
        User user = new User();
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(new ArrayList<>());

        for (RoleCode roleCode : roleCodes) {
            Role role = roleRepository.findByRoleCode(roleCode);
            user.getRoles().add(role);
        }
        String passwordHash = passwordTool.hashPassword(password);
        user.setPasswordHash(passwordHash);
        userRepository.save(user);

        Account account = new Account();
        account.setOwner(user);
        account.setAccountName(userName + " Account");
        account.setBalance(initialBalance);
        account.setAccountCode(accountCode);
        accountRepository.save(account);

        user.setAccount(account);

        // New accounts need one interest accrual transaction
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setAccount(account);
        accountTransaction.setAmount(BigDecimal.ZERO);
        accountTransaction.setAccountBalanceAfterTransaction(initialBalance);
        accountTransaction.setTransactionType(TransactionType.INTEREST_ACCRUAL);
        accountTransaction.setDescription(INITIAL_ACCOUNT_TRANSACTION_DESCRIPTION);

        accountTransactionRepository.save(accountTransaction);

        return user;
    }

    @Override
    public User saveNewPassword(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        String passwordHash = passwordTool.hashPassword(password);
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
