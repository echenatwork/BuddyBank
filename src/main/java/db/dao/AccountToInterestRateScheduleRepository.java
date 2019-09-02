package db.dao;

import db.entity.AccountToInterestRateSchedule;
import org.springframework.data.repository.CrudRepository;

public interface AccountToInterestRateScheduleRepository extends CrudRepository<AccountToInterestRateSchedule, Long> {
}