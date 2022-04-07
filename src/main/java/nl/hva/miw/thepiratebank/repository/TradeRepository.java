package nl.hva.miw.thepiratebank.repository;

import nl.hva.miw.thepiratebank.domain.*;
import nl.hva.miw.thepiratebank.repository.order.OrderDAO;
import nl.hva.miw.thepiratebank.repository.rootrepositories.AssetRateRootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.AssetRootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.CustomerRootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.UserRootRepository;
import nl.hva.miw.thepiratebank.repository.transaction.TransactionDAO;
import nl.hva.miw.thepiratebank.domain.Order;
import nl.hva.miw.thepiratebank.utilities.exceptions.ConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Seperated root repository for Trade model and Transaction model */

@Repository
public class TradeRepository {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final TransactionDAO transactionDAO;
    private final OrderDAO orderDAO;
    private final AssetRootRepository assetRootRepository;
    private final RootRepository rootRepository;


    public TradeRepository(TransactionDAO transactionDAO, OrderDAO orderDAO, RootRepository rootRepository,AssetRootRepository assetRootRepository,
                           CustomerRootRepository customerRootRepository) {
        this.transactionDAO = transactionDAO;
        this.orderDAO = orderDAO;
        this.rootRepository = rootRepository;
        this.assetRootRepository = assetRootRepository;
    }

    /**
-ma     * CRUD Methods for Transaction flow
     */
    public List<Transaction> findAllTransactions() {
        return transactionDAO.findAll().stream().map(e -> buildTransactionAssociations(e)).collect(Collectors.toList());
    }

    public void saveTransaction(Transaction transaction) {
        transactionDAO.save(transaction);
    }

    public Optional<Transaction> findTransaction(Integer id) {
            Optional<Transaction> transactionOptional = transactionDAO.find(id);
            transactionOptional.ifPresent(this::buildTransactionAssociations);
        return transactionOptional;
    }

    public void updateTransaction (Transaction transaction) {
        transactionDAO.update(transaction);
    }

    public void deleteTransaction(Integer id) {
        transactionDAO.delete(id);
    }

    public List<Transaction> findBuyTransactionsByUserId(Integer id) {
        List<Transaction> transactionlist = transactionDAO.findBuyTransactionsByUserId(id);
        transactionlist.stream().map(e -> buildTransactionAssociations(e)).collect(Collectors.toList());
        return transactionlist;
    }

    public List<Transaction> findSellTransactionsByUserId(Integer id) {
        List<Transaction> transactionlist = transactionDAO.findSellTransactionsByUserId(id);
        transactionlist.stream().map(e -> buildTransactionAssociations(e)).collect(Collectors.toList());
        return transactionlist;
    }


    public Transaction buildTransactionAssociations(Transaction transaction) {
        transaction.setBuyer(rootRepository.findBuyerOfTransaction(transaction.getTransactionId())
                .orElseThrow(() -> new ConflictException("An error occured building transactions.")));
        transaction.setSeller(rootRepository.findSellerOfTransaction(transaction.getTransactionId())
                .orElseThrow(() -> new ConflictException("An error occured building transactions.")));
        transaction.setAsset(assetRootRepository.findAssetOfTransaction(transaction.getTransactionId())
                .orElseThrow(() -> new ConflictException("An error occured building transactions.")));
        return transaction;
    }

    public Optional<Transaction> findTransactionById(Integer id) {
        Optional<Transaction> transactionOption = transactionDAO.find(id);
        if (transactionOption.isPresent()) {
            Transaction transaction = transactionOption.get();
           transaction = buildTransactionAssociations(transaction);
            return Optional.of(transaction);
        }
        return Optional.empty();
    }

    /**
     * CRUD Methods for Order flow
     */

    public List<Order> findAllOrders() {
        return orderDAO.findAll().stream()
                .map(e -> buildOrderAssociations(e))
                .collect(Collectors.toList());
    }

    public void saveOrder(Order order) {
        orderDAO.save(order);
    }

    public Optional<Order> findOrder (Integer id) {
        Optional<Order> orderOptional = orderDAO.find(id);
        orderOptional.ifPresent(this::buildOrderAssociations);
        return orderOptional;
    }

    public List<Order> findAllOrdersByUserId (int userid) {
        List<Order> orders = orderDAO.findAllOrdersByUserId(userid);
        return orders.stream().map(e -> buildOrderAssociations(e)).collect(Collectors.toList());

    }


    public void deleteOrder(Integer id){
            orderDAO.delete(id);
    }

    public void updateOrder (Order order) {
        orderDAO.update(order);
    }

    public Order buildOrderAssociations(Order order) {
        order.setUser(rootRepository.findCustomerOfOrder(order.getOrderId()).get());
        order.setAsset(assetRootRepository.findAssetOfOrder(order.getOrderId()).get());
        return order;
    }
}

