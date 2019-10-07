package manager.entity;

import java.math.BigDecimal;
import java.util.Date;

public class InterestPayment {
    private Date date;
    private BigDecimal interestAmount;

    public InterestPayment() {
    }

    public InterestPayment(Date date, BigDecimal interestAmount) {
        this.date = date;
        this.interestAmount = interestAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }
}
