package manager;

import db.entity.InterestRateSchedule;
import db.entity.InterestRateScheduleBucket;
import error.RequestException;

import java.util.Collection;
import java.util.List;

public interface InterestRateScheduleManager {
    public InterestRateSchedule saveInterestRateSchedule(String code, String name, Collection<InterestRateScheduleBucket> interestRateScheduleBuckets) throws RequestException;

    public List<String> getInterestRateScheduleCodes();

    public InterestRateSchedule getInterestRateScheduleByCode(String code);

    public List<InterestRateSchedule> getInterestRateSchedules();
}
