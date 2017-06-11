package manager;

import db.dao.AccountRepository;
import db.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 6/8/2017.
 */
@Component
public class AccountManagerImpl implements AccountManager {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account findByAccountCode(String accountCode) {
        return accountRepository.findByAccountCode(accountCode);
    }

    @Override
    public List<Account> getAllAccounts() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return accountRepository.findAll(sort);
    }
}
