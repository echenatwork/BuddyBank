package manager;

import db.entity.Account;
import db.entity.AccountToInterestRateSchedule;

import java.util.Date;
import java.util.List;

/**
 * Created by Eric on 6/8/2017.
 */
public interface AccountManager {

    public Account findByAccountCode(String accountCode);

    public List<Account> getAllAccounts();

    public Account addScheduleToAccount(Date startDate, Date endDate, String accountCode, String scheduleCode);

    public void deleteScheduleFromAccount(Long id);
}
