package db.dao;

import db.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Eric on 3/25/2017.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findByAccountCode(String accountCode);

}
