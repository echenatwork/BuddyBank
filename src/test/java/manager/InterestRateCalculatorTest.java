package manager;

import db.entity.AccountToInterestRateSchedule;
import db.entity.InterestRateSchedule;
import db.entity.InterestRateScheduleBucket;
import manager.entity.InterestPayment;
import manager.entity.InterestRateCalculatorResults;
import org.junit.Test;
import util.Utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static util.TestUtils.assertRoughlyEquals;

public class InterestRateCalculatorTest {

    private static final String CRON_FRIDAYS = "0 0 0 * * FRI";


    @Test
    public void calculateInterestPaymentsBetweenDates_oneInterestPayment() throws Exception {
        // setup
        InterestRateCalculator fixture = new InterestRateCalculator(CRON_FRIDAYS);
        Instant startDate = Utils.parseLocalTimeZoneInstant("2019-10-08T17:00:00");
        Instant endDate = Utils.parseLocalTimeZoneInstant("2019-10-15T17:00:00");
        BigDecimal initialAmount = new BigDecimal("100");

        // TODO create interest rate schedule builder
        InterestRateScheduleBucket bucket = new InterestRateScheduleBucket();
        bucket.setAmountFloor(new BigDecimal("0"));
        bucket.setAmountCeiling(new BigDecimal("1000000"));
        bucket.setInterestRate(new BigDecimal("0.1"));
        List<InterestRateScheduleBucket> interestRateScheduleBuckets = new ArrayList<>();
        interestRateScheduleBuckets.add(bucket);
        InterestRateSchedule interestRateSchedule = new InterestRateSchedule();
        interestRateSchedule.setInterestRateScheduleBuckets(interestRateScheduleBuckets);
        AccountToInterestRateSchedule accountToInterestRateSchedule = new AccountToInterestRateSchedule();
        accountToInterestRateSchedule.setInterestRateSchedule(interestRateSchedule);
        accountToInterestRateSchedule.setStartDateTime(startDate.minus(1, ChronoUnit.DAYS));
        accountToInterestRateSchedule.setEndDateTime(endDate.plus(1, ChronoUnit.DAYS));

        // execute
        InterestRateCalculatorResults actual = fixture.calculateInterestPaymentsBetweenDates(startDate, endDate, initialAmount, Arrays.asList(accountToInterestRateSchedule));

        // verify
        assertTrue(actual.getErrorMessages().isEmpty());
        assertEquals(1, actual.getInterestPayments().size());
        InterestPayment actualInterestPayment = actual.getInterestPayments().get(0);
        assertRoughlyEquals(new BigDecimal("10"), actualInterestPayment.getInterestAmount());
        assertRoughlyEquals(new BigDecimal("110"), actualInterestPayment.getNewBalance());
        assertEquals(Utils.parseLocalTimeZoneInstant("2019-10-11T00:00:00"), actualInterestPayment.getDate());
    }
}
