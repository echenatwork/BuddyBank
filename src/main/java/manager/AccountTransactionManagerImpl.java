package manager;

import db.dao.AccountRepository;
import db.dao.AccountTransactionRepository;
import db.entity.Account;
import db.entity.AccountTransaction;
import db.entity.TransactionType;
import error.PersistentException;
import error.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Eric on 6/9/2017.
 */
@Component
public class AccountTransactionManagerImpl implements AccountTransactionManager {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;


    @Override
    @Transactional
    public AccountTransaction transferAmount(String senderAccountCode, BigDecimal transferAmount, String receiverAccountCode, String transferDescription) {

        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RequestException("Transfer amount: " + transferAmount + " is not allowed. Amounts must be greater than 0");
        }
        if (Objects.equals(senderAccountCode, receiverAccountCode)) {
            throw new RequestException("Sender account (" + senderAccountCode + ") must be different than receiver account (" + receiverAccountCode + ")");
        }

        // TODO validate precision/scale isn't below fraction of a cent


        Account senderAccount = accountRepository.findByAccountCode(senderAccountCode);
        Account receiverAccount = accountRepository.findByAccountCode(receiverAccountCode);

        BigDecimal newSendingBalance = senderAccount.getBalance().subtract(transferAmount);

        if (newSendingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RequestException(senderAccountCode + " does not have sufficient balance for this transaction");
        }

        senderAccount.setBalance(newSendingBalance);
        accountRepository.save(senderAccount);

        Date transactionDate = new Date();
        AccountTransaction sendingTransaction = new AccountTransaction();
        sendingTransaction.setTransactionType(TransactionType.TRANSFER_SEND);
        sendingTransaction.setAccount(senderAccount);
        sendingTransaction.setAmount(transferAmount);
        sendingTransaction.setDescription(transferDescription);
        sendingTransaction.setTransactionDate(transactionDate);
        sendingTransaction.setAccountBalanceAfterTransaction(newSendingBalance);
        accountTransactionRepository.save(sendingTransaction);

        BigDecimal newReceivingAccountBalance = receiverAccount.getBalance().add(transferAmount);
        receiverAccount.setBalance(newReceivingAccountBalance);
        accountRepository.save(receiverAccount);


        AccountTransaction receivingTransaction = new AccountTransaction();
        receivingTransaction.setTransactionType(TransactionType.TRANSFER_RECEIVE);
        receivingTransaction.setAccount(receiverAccount);
        receivingTransaction.setAmount(transferAmount);
        receivingTransaction.setDescription(transferDescription);
        receivingTransaction.setTransactionDate(transactionDate);
        receivingTransaction.setAccountBalanceAfterTransaction(newReceivingAccountBalance);
        accountTransactionRepository.save(receivingTransaction);


        sendingTransaction.setRelatedTransaction(receivingTransaction);
        receivingTransaction.setRelatedTransaction(sendingTransaction);

        accountTransactionRepository.save(sendingTransaction);
        accountTransactionRepository.save(receivingTransaction);

        return sendingTransaction;
    }
}
