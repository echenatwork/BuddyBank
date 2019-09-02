package web.model;

import java.math.BigDecimal;
import java.util.List;

public class AccountBean {
    private String code;
    private BigDecimal balance;
    private String name;
    private List<AccountToScheduleBean> accountToSchedules;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AccountToScheduleBean> getAccountToSchedules() {
        return accountToSchedules;
    }

    public void setAccountToSchedules(List<AccountToScheduleBean> accountToSchedules) {
        this.accountToSchedules = accountToSchedules;
    }
}
