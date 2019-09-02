package manager;

import db.dao.InterestRateScheduleRepository;
import db.entity.InterestRateSchedule;
import db.entity.InterestRateScheduleBucket;
import error.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class InterestRateScheduleManagerImpl implements InterestRateScheduleManager {

    @Autowired
    private InterestRateScheduleRepository interestRateScheduleRepository;

    @Override
    public InterestRateSchedule saveInterestRateSchedule(String code, String name, Collection<InterestRateScheduleBucket> interestRateScheduleBuckets) throws RequestException {

        InterestRateSchedule interestRateSchedule = interestRateScheduleRepository.findByInterestRateScheduleCode(code);
        if (interestRateSchedule == null) {
            interestRateSchedule = new InterestRateSchedule();
            interestRateSchedule.setInterestRateScheduleBuckets(new ArrayList<>());
            interestRateSchedule.setInterestRateScheduleCode(code);
        } else {
            for (InterestRateScheduleBucket bucket : interestRateSchedule.getInterestRateScheduleBuckets()) {
                bucket.setInterestRateSchedule(null);
            }
            interestRateSchedule.getInterestRateScheduleBuckets().clear();
        }
        interestRateSchedule.setName(name);
        interestRateSchedule.getInterestRateScheduleBuckets().addAll(interestRateScheduleBuckets);

        for (InterestRateScheduleBucket interestRateScheduleBucket : interestRateScheduleBuckets) {
            interestRateScheduleBucket.setInterestRateSchedule(interestRateSchedule);
        }

        validateInterestRateSchedule(interestRateSchedule);

        InterestRateSchedule saved = interestRateScheduleRepository.save(interestRateSchedule);
        return saved;
    }

    private void validateInterestRateSchedule(InterestRateSchedule interestRateSchedule) throws RequestException {
        List<InterestRateScheduleBucket> buckets = interestRateSchedule.getInterestRateScheduleBuckets();

        if (CollectionUtils.isEmpty(buckets)) {
            throw new RequestException("Schedule must have at least one bucket");
        }

        buckets.sort((a, b) -> {
            if (a.getAmountFloor() == null && b.getAmountFloor() == null) {
                return 0;
            } else if (a.getAmountFloor() == null) {
                return -1;
            } else if (b.getAmountFloor() == null) {
                return 1;
            }
            return a.getAmountFloor().compareTo(b.getAmountFloor());
        });

        if (buckets.get(0).getAmountFloor() != null) {
            throw new RequestException("Smallest bucket floor value needs to be null");
        }

        if (buckets.get(buckets.size() - 1).getAmountCeiling() != null) {
            throw new RequestException("Largest bucket ceiling needs to be null");
        }

        for (int i = 1; i < buckets.size(); i++) {
            InterestRateScheduleBucket curr = buckets.get(i);
            InterestRateScheduleBucket left = buckets.get(i - 1);
            if (left.getAmountCeiling().equals(curr.getAmountFloor())) {
                throw new RequestException("Gap between values " + left.getAmountCeiling().toPlainString() + " and " + curr.getAmountCeiling().toPlainString());
            }
        }
    }

    @Override
    public List<String> getInterestRateScheduleCodes() {
        Iterable<InterestRateSchedule> interestRateSchedules = interestRateScheduleRepository.findAll();

        List<String> codes = new ArrayList<>();
        for (InterestRateSchedule interestRateSchedule : interestRateSchedules) {
            codes.add(interestRateSchedule.getInterestRateScheduleCode());
        }
        return codes;
    }

    @Override
    public InterestRateSchedule getInterestRateScheduleByCode(String code) {
        return interestRateScheduleRepository.findByInterestRateScheduleCode(code);
    }
}
