package db.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Eric on 3/25/2017.
 */
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "account_code")
    private String accountCode;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "account_name")
    private String accountName;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @OrderBy("transaction_date DESC")
    private List<AccountTransaction> accountTransactions;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @OrderBy("start_date_time ASC")
    private List<AccountToInterestRateSchedule> accountToInterestRateSchedules;

    @Version
    @Column(name = "version")
    private Long version;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<AccountTransaction> getAccountTransactions() {
        return accountTransactions;
    }

    public void setAccountTransactions(List<AccountTransaction> accountTransactions) {
        this.accountTransactions = accountTransactions;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<AccountToInterestRateSchedule> getAccountToInterestRateSchedules() {
        return accountToInterestRateSchedules;
    }

    public void setAccountToInterestRateSchedules(List<AccountToInterestRateSchedule> accountToInterestRateSchedules) {
        this.accountToInterestRateSchedules = accountToInterestRateSchedules;
    }
}
