package web.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateInterestRateScheduleRequest {
    private String code;
    private String name;
    private List<InterestRateBucket> interestRateBuckets = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InterestRateBucket> getInterestRateBuckets() {
        return interestRateBuckets;
    }

    public void setInterestRateBuckets(List<InterestRateBucket> interestRateBuckets) {
        this.interestRateBuckets = interestRateBuckets;
    }

    public static class InterestRateBucket {
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
}
