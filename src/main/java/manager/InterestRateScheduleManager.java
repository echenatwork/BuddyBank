package manager;

import db.entity.InterestRateSchedule;
import db.entity.InterestRateScheduleBucket;
import error.RequestException;

import java.util.Collection;

public interface InterestRateScheduleManager {
    public InterestRateSchedule createInterestRateSchedule(String code, String name, Collection<InterestRateScheduleBucket> interestRateScheduleBuckets) throws RequestException;
}
