package db.entity.builder;

import db.entity.AccountToInterestRateSchedule;
import db.entity.InterestRateSchedule;
import db.entity.InterestRateScheduleBucket;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

public final class InterestRateScheduleBuilder {
    private Long id;
    private String interestRateScheduleCode;
    private String name;
    private List<InterestRateScheduleBucket> interestRateScheduleBuckets;
    private List<AccountToInterestRateSchedule> accountToInterestRateSchedules;

    private InterestRateScheduleBuilder() {
    }

    public static InterestRateScheduleBuilder anInterestRateSchedule() {
        return new InterestRateScheduleBuilder();
    }

    public InterestRateScheduleBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public InterestRateScheduleBuilder withInterestRateScheduleCode(String interestRateScheduleCode) {
        this.interestRateScheduleCode = interestRateScheduleCode;
        return this;
    }

    public InterestRateScheduleBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public InterestRateScheduleBuilder withInterestRateScheduleBucket(InterestRateScheduleBucket interestRateScheduleBucket) {
        if (this.interestRateScheduleBuckets == null) {
            this.interestRateScheduleBuckets = new ArrayList<>();
        }
        this.interestRateScheduleBuckets.add(interestRateScheduleBucket);
        return this;
    }

    public InterestRateScheduleBuilder withInterestRateScheduleBuckets(List<InterestRateScheduleBucket> interestRateScheduleBuckets) {
        this.interestRateScheduleBuckets = interestRateScheduleBuckets;
        return this;
    }

    public InterestRateScheduleBuilder withAccountToInterestRateSchedules(List<AccountToInterestRateSchedule> accountToInterestRateSchedules) {
        this.accountToInterestRateSchedules = accountToInterestRateSchedules;
        return this;
    }

    public InterestRateSchedule build() {
        InterestRateSchedule interestRateSchedule = new InterestRateSchedule();

        for (InterestRateScheduleBucket interestRateScheduleBucket :
                Utils.emptyIfNull(interestRateScheduleBuckets)) {
            interestRateScheduleBucket.setInterestRateSchedule(interestRateSchedule);
        }

        for (AccountToInterestRateSchedule accountToInterestRateSchedule :
                Utils.emptyIfNull(accountToInterestRateSchedules)) {
            accountToInterestRateSchedule.setInterestRateSchedule(interestRateSchedule);
        }

        interestRateSchedule.setId(id);
        interestRateSchedule.setInterestRateScheduleCode(interestRateScheduleCode);
        interestRateSchedule.setName(name);
        interestRateSchedule.setInterestRateScheduleBuckets(interestRateScheduleBuckets);
        interestRateSchedule.setAccountToInterestRateSchedules(accountToInterestRateSchedules);
        return interestRateSchedule;
    }
}
