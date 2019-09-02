package web.model;

import java.math.BigDecimal;

public class ScheduleBucketBean {
    private BigDecimal amountFloor;
    private BigDecimal amountCeiling;
    private BigDecimal interestRate;

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
