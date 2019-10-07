package manager;

import db.entity.AccountToInterestRateSchedule;
import manager.entity.InterestPayment;
import manager.entity.InterestRateCalculatorResults;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Simple interest rate calculator that calculates the interest to be paid every Friday
 */
public class InterestRateCalculator {

    public InterestRateCalculator(String cronInterestExpression) {
        this.cronSequenceGenerator = new CronSequenceGenerator(cronInterestExpression); // Midnight on Fridays
    }

    private CronSequenceGenerator cronSequenceGenerator;

    public InterestRateCalculatorResults calculateInterestPaymentsBetweenDates(Date startDate, Date endDate,
                                                                               BigDecimal initialAmount,
                                                                               List<AccountToInterestRateSchedule> accountToInterestRateSchedules) {
        List<InterestPayment> interestPayments = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();


        InterestRateCalculatorResults interestRateCalculatorResults = new InterestRateCalculatorResults();
        interestRateCalculatorResults.setInterestPayments(interestPayments);
        interestRateCalculatorResults.setErrorMessages(errorMessages);
        return interestRateCalculatorResults;
    }

    public static void main(String[] args) {
        InterestRateCalculator interestRateCalculator = new InterestRateCalculator("0 0 0 * * FRI");
        Date date = new Date(123L);
        Date date2 = new Date(123L);
        interestRateCalculator.cronSequenceGenerator.next(new Date());
    }
}
