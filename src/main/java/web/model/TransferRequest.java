package web.model;

/**
 * Created by Eric on 6/7/2017.
 */
public class TransferRequest {
    private String amount;
    private String description;
    private String receiverAccountCode;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiverAccountCode() {
        return receiverAccountCode;
    }

    public void setReceiverAccountCode(String receiverAccountCode) {
        this.receiverAccountCode = receiverAccountCode;
    }
}
