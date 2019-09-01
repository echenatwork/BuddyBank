package db.dao;

import db.entity.InterestRateSchedule;
import org.springframework.data.repository.CrudRepository;

public interface InterestRateScheduleRepository extends CrudRepository<InterestRateSchedule, Long> {

    public InterestRateSchedule findByInterestRateScheduleCode(String interestRateScheduleCode);
}
