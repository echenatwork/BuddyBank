package db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

/**
 * Floor is inclusive, ceiling is exclusive
 */
@Entity
public class InterestRateScheduleBucket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_rate_schedule_bucket_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interest_rate_schedule_id")
    private InterestRateSchedule interestRateSchedule;

    @Column(name = "amount_floor")
    private BigDecimal amountFloor;

    @Column(name = "amount_ceiling")
    private BigDecimal amountCeiling;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

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

    public BigDecimal getAmountFloor() {
        return amountFloor;
    }

    public void setAmountFloor(BigDecimal amountFloor) {
        this.amountFloor = amountFloor;
    }

    public BigDecimal getAmountCeiling() {
        return amountCeiling;
    }

    public void setAmountCeiling(BigDecimal amountCeiling) {
        this.amountCeiling = amountCeiling;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
