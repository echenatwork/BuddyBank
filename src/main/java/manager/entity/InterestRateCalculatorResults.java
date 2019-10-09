package manager.entity;

import java.util.ArrayList;
import java.util.List;

public class InterestRateCalculatorResults {
    List<InterestPayment> interestPayments;
    List<String> errorMessages;

    public InterestRateCalculatorResults() {
        this.interestPayments = new ArrayList<>();
        this.errorMessages = new ArrayList<>();
    }

    public List<InterestPayment> getInterestPayments() {
        return interestPayments;
    }

    public void setInterestPayments(List<InterestPayment> interestPayments) {
        this.interestPayments = interestPayments;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
