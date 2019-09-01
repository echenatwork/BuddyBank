package db.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;

@Entity
public class InterestRateSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_rate_schedule_id")
    private Long id;

    @Column(name = "interest_rate_schedule_code")
    private String interestRateScheduleCode;

    @Column(name = "interest_rate_schedule_name")
    private String name;

    @OneToMany(mappedBy = "interestRateSchedule", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("amount_floor ASC")
    private List<InterestRateScheduleBucket> interestRateScheduleBuckets;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @OrderBy("start_date_time ASC")
    private List<AccountToInterestRateSchedule> accountToInterestRateSchedules;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInterestRateScheduleCode() {
        return interestRateScheduleCode;
    }

    public void setInterestRateScheduleCode(String code) {
        this.interestRateScheduleCode = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InterestRateScheduleBucket> getInterestRateScheduleBuckets() {
        return interestRateScheduleBuckets;
    }

    public void setInterestRateScheduleBuckets(List<InterestRateScheduleBucket> interestRateScheduleBuckets) {
        this.interestRateScheduleBuckets = interestRateScheduleBuckets;
    }

    public List<AccountToInterestRateSchedule> getAccountToInterestRateSchedules() {
        return accountToInterestRateSchedules;
    }

    public void setAccountToInterestRateSchedules(List<AccountToInterestRateSchedule> accountToInterestRateSchedules) {
        this.accountToInterestRateSchedules = accountToInterestRateSchedules;
    }
}
