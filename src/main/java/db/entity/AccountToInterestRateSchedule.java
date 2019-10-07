package db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.Date;

@Entity
public class AccountToInterestRateSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_to_interest_rate_schedule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interest_rate_schedule_id")
    private InterestRateSchedule interestRateSchedule;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "start_date_time")
    private Instant startDateTime;

    @Column(name = "end_date_time")
    private Instant endDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InterestRateSchedule getInterestRateSchedule() {
        return interestRateSchedule;
    }

    public void setInterestRateSchedule(InterestRateSchedule interestRateSchedule) {
        this.interestRateSchedule = interestRateSchedule;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Instant getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Instant startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Instant getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }
}
