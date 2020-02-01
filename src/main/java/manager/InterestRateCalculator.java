package manager;

import db.entity.AccountToInterestRateSchedule;
import db.entity.InterestRateSchedule;
import db.entity.InterestRateScheduleBucket;
import error.RequestException;
import manager.entity.InterestPayment;
import manager.entity.InterestRateCalculatorResults;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Simple interest rate calculator that calculates the interest to be paid by the given cron expression
 * Default is Midnight on Friday
 */
public class InterestRateCalculator {

    public InterestRateCalculator(String cronInterestExpression) {
        this.cronSequenceGenerator = new CronSequenceGenerator(cronInterestExpression); // Midnight on Fridays
    }

    private CronSequenceGenerator cronSequenceGenerator;

    // Returns all interest rate events [startDate, endDate]
    public InterestRateCalculatorResults calculateInterestPaymentsBetweenDates(Instant startDate,
                                                                               Instant endDate,
                                                                               BigDecimal initialAmount,
                                                                               List<AccountToInterestRateSchedule> accountToInterestRateSchedules) {
        InterestRateCalculatorResults interestRateCalculatorResults = new InterestRateCalculatorResults();
        if (endDate.isBefore(startDate)) {
            interestRateCalculatorResults.getErrorMessages().add("End date is before start date");
            return interestRateCalculatorResults;
        }

        List<InterestPayment> interestPayments = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        BigDecimal currentAmount = initialAmount;

        Instant now = startDate.minusSeconds(1); // Setting instant to just before start date so we can ensure that the start date is included in the cron evaluation
        now = cronSequenceGenerator.next(Date.from(now)).toInstant();
        while (now.isBefore(endDate) || now.equals(endDate)) {

            BigDecimal interestRate = null;
            try {
                interestRate = getInterestRate(now, currentAmount, accountToInterestRateSchedules);
            } catch (RequestException e) {
                interestRateCalculatorResults.getErrorMessages().add(e.getMessage());
                return interestRateCalculatorResults;
            }

            BigDecimal interestAmount = currentAmount.multiply(interestRate);
            currentAmount = currentAmount.add(interestAmount);

            InterestPayment interestPayment = new InterestPayment(now, interestAmount, currentAmount);
            interestPayments.add(interestPayment);

            now = cronSequenceGenerator.next(Date.from(now)).toInstant();
        }

        interestRateCalculatorResults.setInterestPayments(interestPayments);
        interestRateCalculatorResults.setErrorMessages(errorMessages);
        return interestRateCalculatorResults;
    }

    private BigDecimal getInterestRate(Instant now, BigDecimal currentAmount, List<AccountToInterestRateSchedule> accountToInterestRateSchedules) throws RequestException {
        InterestRateSchedule interestRateSchedule = null;

        for (AccountToInterestRateSchedule accountToInterestRateSchedule : accountToInterestRateSchedules) {
            if ((accountToInterestRateSchedule.getStartDateTime().isBefore(now) || accountToInterestRateSchedule.getStartDateTime().equals(now)) &&
                    accountToInterestRateSchedule.getEndDateTime().isAfter(now)) {
                interestRateSchedule = accountToInterestRateSchedule.getInterestRateSchedule();
                break;
            }
        }

        if (interestRateSchedule == null) {
            throw new RequestException("No valid interest rate found for amount " + currentAmount + " and date " + now);
        }

        for (InterestRateScheduleBucket bucket : interestRateSchedule.getInterestRateScheduleBuckets()) {
            BigDecimal floor = bucket.getAmountFloor();
            BigDecimal ceiling = bucket.getAmountCeiling();
            if ((floor == null || floor.compareTo(currentAmount) == -1 || floor.compareTo(currentAmount) == 0 )
                && (ceiling == null || ceiling.compareTo(currentAmount) == 1)
            ) {
                return bucket.getInterestRate();
            }
        }

        throw new RequestException("No valid interest rate found for amount " + currentAmount + " and date " + now);
    }
}
