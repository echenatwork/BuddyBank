package db.entity;

/**
 * Created by Eric on 4/1/2017.
 */
public enum TransactionType {
    ADMIN,              // This is meant to be used for unusual actions which are meant to correct errors
    ADMIN_DEPOSIT,      // This is meant to be used for the admins creating deposits as part of regular events or rewards
    DEPOSIT,
    INTEREST_ACCRUAL,
    TRANSFER_SEND,
    TRANSFER_RECEIVE,
    WITHDRAWAL
}
