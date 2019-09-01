package manager;

import db.dao.InterestRateScheduleRepository;
import db.entity.InterestRateSchedule;
import db.entity.InterestRateScheduleBucket;
import error.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class InterestRateScheduleManagerImpl implements InterestRateScheduleManager {

    @Autowired
    private InterestRateScheduleRepository interestRateScheduleRepository;

    @Override
    public InterestRateSchedule createInterestRateSchedule(String code, String name, Collection<InterestRateScheduleBucket> interestRateScheduleBuckets) throws RequestException {
        InterestRateSchedule interestRateSchedule = new InterestRateSchedule();
        interestRateSchedule.setInterestRateScheduleCode(code);
        interestRateSchedule.setName(name);
        interestRateSchedule.setInterestRateScheduleBuckets(new ArrayList<>());
        interestRateSchedule.getInterestRateScheduleBuckets().addAll(interestRateScheduleBuckets);

        for (InterestRateScheduleBucket interestRateScheduleBucket : interestRateScheduleBuckets) {
            interestRateScheduleBucket.setInterestRateSchedule(interestRateSchedule);
        }

        // TODO validate buckets
        // One floor value must be null, if not create a default floor value
        // One ceiling value must be null, if not create a ceiling value
        // Make sure no gaps

        InterestRateSchedule saved = interestRateScheduleRepository.save(interestRateSchedule);
        return saved;
    }

    private void validateInterestRateSchedule(InterestRateSchedule interestRateSchedule) throws RequestException {
        
    }
}
