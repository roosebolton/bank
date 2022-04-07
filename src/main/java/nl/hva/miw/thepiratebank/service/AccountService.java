package nl.hva.miw.thepiratebank.service;


import nl.hva.miw.thepiratebank.domain.Account;
import nl.hva.miw.thepiratebank.domain.Transaction;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.AccountRootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final int BANK = 1000;
    private final BigDecimal SHARE_PERCENTAGE= new BigDecimal(0.50);

    private AccountRootRepository accountRootRepository;

    @Autowired
    public AccountService(AccountRootRepository accountRootRepository) {
        this.accountRootRepository = accountRootRepository;
    }

    public Account getAccountById(int customerId){
        return accountRootRepository.getAccountById(customerId);
    }

    public void createAccount(Account account){accountRootRepository.createAccount(account);}

    public void doDebitAndCreditBalance(Transaction transaction){
        doDebitBalance(transaction);
        doCreditBalance(transaction);
        doCreditTransactioncostBankAccount(transaction.getTransactionCost());
    }

    public void doDebitBalance(Transaction transaction){
        Account buyerAccount = accountRootRepository.getAccountById(transaction.getBuyer().getUserId());
        BigDecimal balanceValueDebited = buyerAccount.getBalance().subtract(transaction.getValue());
        buyerAccount.setBalance(balanceValueDebited);
        accountRootRepository.updateAccount(buyerAccount);
        if ((transaction.getBuyer().getUserId() != BANK) && (transaction.getSeller().getUserId() != BANK)){
            BigDecimal transactionCostSplitted = splitTransactionCost(transaction.getTransactionCost());
            doUpdateBalance(buyerAccount, transactionCostSplitted);
        }
        if ((transaction.getBuyer().getUserId() != BANK) && (transaction.getSeller().getUserId() == BANK)){
            doUpdateBalance(buyerAccount, transaction.getTransactionCost());
        }
    }

    public void doCreditBalance(Transaction transaction){
        Account sellerAccount = accountRootRepository.getAccountById(transaction.getSeller().getUserId());
        BigDecimal balanceValueCredited = sellerAccount.getBalance().add(transaction.getValue());
        sellerAccount.setBalance(balanceValueCredited);
        accountRootRepository.updateAccount(sellerAccount);
        if ((transaction.getBuyer().getUserId() != BANK) && (transaction.getSeller().getUserId() != BANK)){
            BigDecimal transactionCostSplitted = splitTransactionCost(transaction.getTransactionCost());;
            doUpdateBalance(sellerAccount, transactionCostSplitted);
        }
        if ((transaction.getBuyer().getUserId() == BANK) && (transaction.getSeller().getUserId() != BANK)){
            doUpdateBalance(sellerAccount, transaction.getTransactionCost());
        }
    }

    public void doCreditTransactioncostBankAccount(BigDecimal transactioncost){
        Account bankAccount = accountRootRepository.getAccountById(BANK);
        if (bankAccount != null) {
            BigDecimal bankBalanceTransactionCostCredited = bankAccount.getBalance().add(transactioncost);
            bankAccount.setBalance(bankBalanceTransactionCostCredited);
            accountRootRepository.updateAccount(bankAccount);
        }
    }


    public BigDecimal splitTransactionCost(BigDecimal transactioncost){
        return transactioncost.multiply(SHARE_PERCENTAGE);
    }

    public void doUpdateBalance(Account account, BigDecimal transactioncost){
        BigDecimal newBalance = account.getBalance().subtract(transactioncost);
        account.setBalance(newBalance);
        accountRootRepository.updateAccount(account);
    }

}
