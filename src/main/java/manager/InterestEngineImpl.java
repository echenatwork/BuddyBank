package manager;

import db.entity.Account;
import db.entity.AccountToInterestRateSchedule;
import db.entity.AccountTransaction;
import manager.entity.InterestPayment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InterestEngineImpl {

    // Fridays an midnight
    private static final String CRON_INTEREST_EXPRESSION = "0 0 0 * * FRI";

    /**
     * Returns a list of interest payments. Interest is paid every Friday.
     *
     * Finds the most recent interest payment, then finds the next date interest should be paid. Checks
     * the account interest schedule and then creates a transaction. Finds the next date that interest should be
     * paid, or if no more dates exist then returns the list of interest to be paid.
     *
     * @param now
     * @param account
     * @return
     */
    public List<AccountTransaction> calculateInterestPayments(Date now, Account account) {
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(now);

        // TODO need to finish adding schedules to accounts first
        account.getAccountToInterestRateSchedules();

        int dayOfWeek = nowCal.get(Calendar.DAY_OF_WEEK);

        LocalDate date = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return null;
    }




}
