package web.controller;

import db.entity.Account;
import db.entity.AccountTransaction;
import db.entity.User;
import manager.AccountManager;
import manager.AccountTransactionManager;
import manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.TransferRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eric on 4/1/2017.
 */
@Controller
public class AccountController {

    @Autowired
    private UserManager userManager;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private AccountTransactionManager accountTransactionManager;

    @GetMapping("/account")
    public String accountView(Model model, HttpServletRequest httpServletRequest) {
        String userName = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userManager.findByUserName(userName);
        Account account = user.getAccount();

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        String formattedBalance = currencyFormatter.format(account.getBalance());

        model.addAttribute("name", user.getFirstName());
        model.addAttribute("currentAccountBalance", formattedBalance);
        model.addAttribute("transactions", account.getAccountTransactions());

        TransferRequest transferRequest = new TransferRequest();
        model.addAttribute("transferRequest", transferRequest);

        List<Account> accounts = accountManager.getAllAccounts();
        Map<String, String> accountCodeToName = new HashMap<>();
        for (Account receiverAccount : accounts) {
            if (!receiverAccount.getAccountCode().equals(account.getAccountCode())) {
                accountCodeToName.put(receiverAccount.getAccountCode(), receiverAccount.getAccountName());
            }
        }

        model.addAttribute("receiverAccounts", accountCodeToName);

        // returns the view name
        return "account";
    }

    @PostMapping("/transfer")
    public String transferView(@ModelAttribute TransferRequest transferRequest, Model model, HttpServletRequest httpServletRequest) {
        String userName = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userManager.findByUserName(userName);
        Account senderAccount = user.getAccount();

        try {
            BigDecimal transferAmount = new BigDecimal(transferRequest.getAmount());
            String transferDescription = transferRequest.getDescription();
            String receiverAccountCode = transferRequest.getReceiverAccountCode();

            accountTransactionManager.transferAmount(senderAccount.getAccountCode(), transferAmount, receiverAccountCode, transferDescription);

            Account receiverAccount = accountManager.findByAccountCode(receiverAccountCode);



        } catch (Exception e) {
            // TODO error message handling
            throw new RuntimeException(e);
        }







        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        String formattedBalance = currencyFormatter.format(senderAccount.getBalance());

        model.addAttribute("name", user.getFirstName());
        model.addAttribute("currentAccountBalance", formattedBalance);
        model.addAttribute("transactions", senderAccount.getAccountTransactions());

        AccountTransaction withdrawalRequest = new AccountTransaction();
        model.addAttribute("withdrawalRequest", withdrawalRequest);

        List<String> receiverAccountCodes = new ArrayList<>();


        // returns the view name
        return "account";
    }

    @GetMapping("/admin/test")
    public String testAdminView(Model model, HttpServletRequest httpServletRequest) {
        String userName = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userManager.findByUserName(userName);
        Account account = user.getAccount();

        model.addAttribute("name", "ADMIN: " + user.getFirstName());

        // returns the view name
        return "account";
    }

}
