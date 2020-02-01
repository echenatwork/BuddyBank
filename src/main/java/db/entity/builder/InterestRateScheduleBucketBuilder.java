package db.entity.builder;

import db.entity.InterestRateSchedule;
import db.entity.InterestRateScheduleBucket;

import java.math.BigDecimal;

public final class InterestRateScheduleBucketBuilder {
    private Long id;
    private InterestRateSchedule interestRateSchedule;
    private BigDecimal amountFloor;
    private BigDecimal amountCeiling;
    private BigDecimal interestRate;

    private InterestRateScheduleBucketBuilder() {
    }

    public static InterestRateScheduleBucketBuilder anInterestRateScheduleBucket() {
        return new InterestRateScheduleBucketBuilder();
    }

    public InterestRateScheduleBucketBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public InterestRateScheduleBucketBuilder withInterestRateSchedule(InterestRateSchedule interestRateSchedule) {
        this.interestRateSchedule = interestRateSchedule;
        return this;
    }

    public InterestRateScheduleBucketBuilder withAmountFloor(BigDecimal amountFloor) {
        this.amountFloor = amountFloor;
        return this;
    }

    public InterestRateScheduleBucketBuilder withAmountCeiling(BigDecimal amountCeiling) {
        this.amountCeiling = amountCeiling;
        return this;
    }

    public InterestRateScheduleBucketBuilder withInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
        return this;
    }

    public InterestRateScheduleBucketBuilder withAmountFloor(String amountFloor) {
        return withAmountFloor(new BigDecimal(amountFloor));
    }

    public InterestRateScheduleBucketBuilder withAmountCeiling(String amountCeiling) {
        return withAmountCeiling(new BigDecimal(amountCeiling));
    }

    public InterestRateScheduleBucketBuilder withInterestRate(String interestRate) {
        return withInterestRate(new BigDecimal(interestRate));
    }

    public InterestRateScheduleBucket build() {
        InterestRateScheduleBucket interestRateScheduleBucket = new InterestRateScheduleBucket();
        interestRateScheduleBucket.setId(id);
        interestRateScheduleBucket.setInterestRateSchedule(interestRateSchedule);
        interestRateScheduleBucket.setAmountFloor(amountFloor);
        interestRateScheduleBucket.setAmountCeiling(amountCeiling);
        interestRateScheduleBucket.setInterestRate(interestRate);
        return interestRateScheduleBucket;
    }
}
