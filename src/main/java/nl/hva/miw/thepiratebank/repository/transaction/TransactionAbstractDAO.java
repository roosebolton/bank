package nl.hva.miw.thepiratebank.repository.transaction;

import nl.hva.miw.thepiratebank.domain.Transaction;
import nl.hva.miw.thepiratebank.repository.GenericDAOWithOptional;

import java.util.List;

public abstract class TransactionAbstractDAO implements GenericDAOWithOptional<Transaction,Integer> {

    abstract List<Transaction> findBuyTransactionsByUserId(Integer id);
    abstract List<Transaction> findSellTransactionsByUserId(Integer id);

}
