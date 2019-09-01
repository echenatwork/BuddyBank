package db.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Eric on 4/1/2017.
 */
@Entity
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_transaction_id")
    private Long id;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "account_balance_after_transaction")
    private BigDecimal accountBalanceAfterTransaction;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "related_account_transaction_id")
    private AccountTransaction relatedTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccountTransaction getRelatedTransaction() {
        return relatedTransaction;
    }

    public void setRelatedTransaction(AccountTransaction relatedTransaction) {
        this.relatedTransaction = relatedTransaction;
    }

    public BigDecimal getAccountBalanceAfterTransaction() {
        return accountBalanceAfterTransaction;
    }

    public void setAccountBalanceAfterTransaction(BigDecimal accountBalanceAfterTransaction) {
        this.accountBalanceAfterTransaction = accountBalanceAfterTransaction;
    }

    @PrePersist
    protected void setCodeAndDate() {
        this.transactionCode = UUID.randomUUID().toString();

        if (transactionDate == null) {
            this.transactionDate = new Date();
        }
    }
}
