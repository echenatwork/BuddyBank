package db.dao;

import db.entity.AccountTransaction;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Eric on 6/9/2017.
 */
public interface AccountTransactionRepository extends CrudRepository<AccountTransaction, Long> {

    AccountTransaction findByTransactionCode(String transactionCode);

}
