package manager;

import db.dao.AccountRepository;
import db.dao.AccountToInterestRateScheduleRepository;
import db.dao.InterestRateScheduleRepository;
import db.entity.Account;
import db.entity.AccountToInterestRateSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Eric on 6/8/2017.
 */
@Component
public class AccountManagerImpl implements AccountManager {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InterestRateScheduleRepository interestRateScheduleRepository;

    @Autowired
    private AccountToInterestRateScheduleRepository accountToInterestRateScheduleRepository;

    @Override
    public Account findByAccountCode(String accountCode) {
        return accountRepository.findByAccountCode(accountCode);
    }

    @Override
    public List<Account> getAllAccounts() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return accountRepository.findAll(sort);
    }

    @Override
    public Account addScheduleToAccount(Date startDate, Date endDate, String accountCode, String scheduleCode) {
        AccountToInterestRateSchedule accountToInterestRateSchedule = new AccountToInterestRateSchedule();
        accountToInterestRateSchedule.setStartDateTime(startDate);
        accountToInterestRateSchedule.setEndDateTime(endDate);
        accountToInterestRateSchedule.setAccount(accountRepository.findByAccountCode(accountCode));
        accountToInterestRateSchedule.setInterestRateSchedule(interestRateScheduleRepository.findByInterestRateScheduleCode(scheduleCode));
        accountToInterestRateScheduleRepository.save(accountToInterestRateSchedule);
        return accountRepository.findByAccountCode(accountCode);
    }

    @Override
    public void deleteScheduleFromAccount(Long id) {
        accountToInterestRateScheduleRepository.deleteById(id);
    }
}
