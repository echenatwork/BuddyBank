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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
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

    private static final String FLASH_MAP_ACCOUNT_MESSAGES = "messages";
    private static final String FLASH_MAP_ACCOUNT_TRANSFER_SUCCESS = "transferSuccessful";
    private static final String FLASH_MAP_ACCOUNT_TRANSFER_REQUEST_MODEL = "transferRequestModel";


    @Autowired
    private UserManager userManager;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private AccountTransactionManager accountTransactionManager;

    @GetMapping("/account")
    public String accountView(Model model, HttpServletRequest httpServletRequest) {
        String userName = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);
        List<String> messages = null;
        if (flashMap != null) {
            messages = (List<String>)flashMap.get(FLASH_MAP_ACCOUNT_MESSAGES);
        }
        model.addAttribute("messages", messages);


        User user = userManager.findByUserName(userName);
        Account account = user.getAccount();

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
        String formattedBalance = currencyFormatter.format(account.getBalance());

        model.addAttribute("name", user.getFirstName());
        model.addAttribute("currentAccountBalance", formattedBalance);
        model.addAttribute("transactions", account.getAccountTransactions());


        // TODO if error, recreate the sending form
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

    // TODO remove model parameter
    @PostMapping("/transfer")
    public String transferView(@ModelAttribute TransferRequest transferRequest, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String userName = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userManager.findByUserName(userName);
        Account senderAccount = user.getAccount();
        boolean isTransferSuccessful = false;
        List<String> messages = new ArrayList<>();
        try {
            BigDecimal transferAmount = new BigDecimal(transferRequest.getAmount());
            String transferDescription = transferRequest.getDescription();
            String receiverAccountCode = transferRequest.getReceiverAccountCode();

            AccountTransaction sendingTransaction = accountTransactionManager.transferAmount(senderAccount.getAccountCode(), transferAmount, receiverAccountCode, transferDescription);

            isTransferSuccessful = true;
            messages.add("Transfer successful - created transcation: " + sendingTransaction.getTransactionCode());
        } catch (Exception e) {
            // TODO add logging
            messages.add(e.getMessage());
            redirectAttributes.addFlashAttribute(FLASH_MAP_ACCOUNT_TRANSFER_REQUEST_MODEL, transferRequest);
        }

        redirectAttributes.addFlashAttribute(FLASH_MAP_ACCOUNT_MESSAGES, messages);
        redirectAttributes.addFlashAttribute(FLASH_MAP_ACCOUNT_TRANSFER_SUCCESS, isTransferSuccessful);

        // returns the view name
        return "redirect:/account";
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
