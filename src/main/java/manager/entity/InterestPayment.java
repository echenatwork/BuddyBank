package manager.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

public class InterestPayment {
    private Instant date;
    private BigDecimal interestAmount;
    private BigDecimal newBalance;

    public InterestPayment() {
    }

    public InterestPayment(Instant date, BigDecimal interestAmount, BigDecimal newBalance) {
        this.date = date;
        this.interestAmount = interestAmount;
        this.newBalance = newBalance;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }
}
