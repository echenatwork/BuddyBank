package manager;

import db.entity.AccountTransaction;

import java.math.BigDecimal;

/**
 * Created by Eric on 6/9/2017.
 */
public interface AccountTransactionManager {

    public AccountTransaction transferAmount(String transfererAccountCode, BigDecimal transferAmount, String receiverAccountCode, String transferDescription);

}
