package manager;

import db.entity.Account;

import java.util.List;

/**
 * Created by Eric on 6/8/2017.
 */
public interface AccountManager {

    public Account findByAccountCode(String accountCode);

    public List<Account> getAllAccounts();
}
