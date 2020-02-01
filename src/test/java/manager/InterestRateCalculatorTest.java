package manager;

import db.entity.AccountToInterestRateSchedule;
import db.entity.InterestRateSchedule;
import db.entity.InterestRateScheduleBucket;
import db.entity.builder.InterestRateScheduleBucketBuilder;
import db.entity.builder.InterestRateScheduleBuilder;
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

        InterestRateSchedule interestRateSchedule = InterestRateScheduleBuilder.anInterestRateSchedule()
                .withInterestRateScheduleBucket(
                        InterestRateScheduleBucketBuilder.anInterestRateScheduleBucket()
                                .withAmountFloor("0")
                                .withAmountCeiling("1000000")
                                .withInterestRate("0.1")
                                .build()
                ).build();
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

    @Test
    public void calculateInterestPaymentsBetweenDates_noInterestPayments() throws Exception {
        // setup
        InterestRateCalculator fixture = new InterestRateCalculator(CRON_FRIDAYS);
        Instant startDate = Utils.parseLocalTimeZoneInstant("2019-10-08T17:00:00");
        Instant endDate = Utils.parseLocalTimeZoneInstant("2019-10-10T17:00:00");
        BigDecimal initialAmount = new BigDecimal("100");

        InterestRateSchedule interestRateSchedule = InterestRateScheduleBuilder.anInterestRateSchedule()
                .withInterestRateScheduleBucket(
                        InterestRateScheduleBucketBuilder.anInterestRateScheduleBucket()
                                .withAmountFloor("0")
                                .withAmountCeiling("1000000")
                                .withInterestRate("0.1")
                                .build()
                ).build();
        AccountToInterestRateSchedule accountToInterestRateSchedule = new AccountToInterestRateSchedule();
        accountToInterestRateSchedule.setInterestRateSchedule(interestRateSchedule);
        accountToInterestRateSchedule.setStartDateTime(startDate.minus(1, ChronoUnit.DAYS));
        accountToInterestRateSchedule.setEndDateTime(endDate.plus(1, ChronoUnit.DAYS));

        // execute
        InterestRateCalculatorResults actual = fixture.calculateInterestPaymentsBetweenDates(startDate, endDate, initialAmount, Arrays.asList(accountToInterestRateSchedule));

        // verify
        assertTrue(actual.getErrorMessages().isEmpty());
        assertEquals(0, actual.getInterestPayments().size());
    }

    @Test
    public void calculateInterestPaymentsBetweenDates_multipleInterestPaymentsCompound() throws Exception {
        // setup
        InterestRateCalculator fixture = new InterestRateCalculator(CRON_FRIDAYS);
        Instant startDate = Utils.parseLocalTimeZoneInstant("2019-10-08T17:00:00");
        Instant endDate = Utils.parseLocalTimeZoneInstant("2019-10-29T17:00:00");
        BigDecimal initialAmount = new BigDecimal("100");

        InterestRateSchedule interestRateSchedule = InterestRateScheduleBuilder.anInterestRateSchedule()
                .withInterestRateScheduleBucket(
                        InterestRateScheduleBucketBuilder.anInterestRateScheduleBucket()
                                .withAmountFloor("0")
                                .withAmountCeiling("1000000")
                                .withInterestRate("0.1")
                                .build()
                ).build();
        AccountToInterestRateSchedule accountToInterestRateSchedule = new AccountToInterestRateSchedule();
        accountToInterestRateSchedule.setInterestRateSchedule(interestRateSchedule);
        accountToInterestRateSchedule.setStartDateTime(startDate.minus(1, ChronoUnit.DAYS));
        accountToInterestRateSchedule.setEndDateTime(endDate.plus(1, ChronoUnit.DAYS));

        // execute
        InterestRateCalculatorResults actual = fixture.calculateInterestPaymentsBetweenDates(startDate, endDate, initialAmount, Arrays.asList(accountToInterestRateSchedule));

        // verify
        assertTrue(actual.getErrorMessages().isEmpty());
        assertEquals(3, actual.getInterestPayments().size());
        InterestPayment actualInterestPayment1 = actual.getInterestPayments().get(0);
        assertRoughlyEquals(new BigDecimal("10"), actualInterestPayment1.getInterestAmount());
        assertRoughlyEquals(new BigDecimal("110"), actualInterestPayment1.getNewBalance());
        assertEquals(Utils.parseLocalTimeZoneInstant("2019-10-11T00:00:00"), actualInterestPayment1.getDate());

        InterestPayment actualInterestPayment2 = actual.getInterestPayments().get(1);
        assertRoughlyEquals(new BigDecimal("11"), actualInterestPayment2.getInterestAmount());
        assertRoughlyEquals(new BigDecimal("121"), actualInterestPayment2.getNewBalance());
        assertEquals(Utils.parseLocalTimeZoneInstant("2019-10-18T00:00:00"), actualInterestPayment2.getDate());

        InterestPayment actualInterestPayment3 = actual.getInterestPayments().get(2);
        assertRoughlyEquals(new BigDecimal("12.1"), actualInterestPayment3.getInterestAmount());
        assertRoughlyEquals(new BigDecimal("133.1"), actualInterestPayment3.getNewBalance());
        assertEquals(Utils.parseLocalTimeZoneInstant("2019-10-25T00:00:00"), actualInterestPayment3.getDate());
    }

    @Test
    public void calculateInterestPaymentsBetweenDates_noValidInterestRateByDateReturnsError() throws Exception {
        // setup
        InterestRateCalculator fixture = new InterestRateCalculator(CRON_FRIDAYS);
        Instant startDate = Utils.parseLocalTimeZoneInstant("2019-10-08T17:00:00");
        Instant endDate = Utils.parseLocalTimeZoneInstant("2019-10-15T17:00:00");
        BigDecimal initialAmount = new BigDecimal("100");

        InterestRateSchedule interestRateSchedule = InterestRateScheduleBuilder.anInterestRateSchedule()
                .withInterestRateScheduleBucket(
                        InterestRateScheduleBucketBuilder.anInterestRateScheduleBucket()
                                .withAmountFloor("0")
                                .withAmountCeiling("1000000")
                                .withInterestRate("0.1")
                                .build()
                ).build();
        AccountToInterestRateSchedule accountToInterestRateSchedule = new AccountToInterestRateSchedule();
        accountToInterestRateSchedule.setInterestRateSchedule(interestRateSchedule);
        accountToInterestRateSchedule.setStartDateTime(startDate.minus(10, ChronoUnit.DAYS));
        accountToInterestRateSchedule.setEndDateTime(startDate.minus(1, ChronoUnit.DAYS));

        // execute
        InterestRateCalculatorResults actual = fixture.calculateInterestPaymentsBetweenDates(startDate, endDate, initialAmount, Arrays.asList(accountToInterestRateSchedule));

        // verify
        assertEquals(1, actual.getErrorMessages().size());
        assertEquals("No valid interest rate found for amount 100 and date 2019-10-11T07:00:00Z", actual.getErrorMessages().get(0));
    }

    @Test
    public void calculateInterestPaymentsBetweenDates_noValidInterestRateByBucketReturnsError() throws Exception {
        // setup
        InterestRateCalculator fixture = new InterestRateCalculator(CRON_FRIDAYS);
        Instant startDate = Utils.parseLocalTimeZoneInstant("2019-10-08T17:00:00");
        Instant endDate = Utils.parseLocalTimeZoneInstant("2019-10-15T17:00:00");
        BigDecimal initialAmount = new BigDecimal("100");

        InterestRateSchedule interestRateSchedule = InterestRateScheduleBuilder.anInterestRateSchedule()
                .withInterestRateScheduleBucket(
                        InterestRateScheduleBucketBuilder.anInterestRateScheduleBucket()
                                .withAmountFloor("1000")
                                .withAmountCeiling("1000000")
                                .withInterestRate("0.1")
                                .build()
                ).build();
        AccountToInterestRateSchedule accountToInterestRateSchedule = new AccountToInterestRateSchedule();
        accountToInterestRateSchedule.setInterestRateSchedule(interestRateSchedule);
        accountToInterestRateSchedule.setStartDateTime(startDate.minus(1, ChronoUnit.DAYS));
        accountToInterestRateSchedule.setEndDateTime(endDate.plus(1, ChronoUnit.DAYS));

        // execute
        InterestRateCalculatorResults actual = fixture.calculateInterestPaymentsBetweenDates(startDate, endDate, initialAmount, Arrays.asList(accountToInterestRateSchedule));

        // verify
        assertEquals(1, actual.getErrorMessages().size());
        assertEquals("No valid interest rate found for amount 100 and date 2019-10-11T07:00:00Z", actual.getErrorMessages().get(0));
    }
}
