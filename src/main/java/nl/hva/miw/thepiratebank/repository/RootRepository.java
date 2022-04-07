package nl.hva.miw.thepiratebank.repository;


import nl.hva.miw.thepiratebank.domain.*;
import nl.hva.miw.thepiratebank.repository.rootrepositories.AssetRootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.CustomerRootRepository;
import nl.hva.miw.thepiratebank.repository.wallethistory.WalletHistoryDAO;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * A RootDepository class, as one intermediate, lets the services use the DAO classes
 */
@Repository
public class RootRepository {

    private final CustomerDAO customerDAO;
    private final WalletHistoryDAO walletHistoryDAO;
    private final AccountDAO accountDAO;
    private final WalletDAO walletDAO;
    private final CustomerRootRepository customerRootRepository;
    private final AssetRootRepository assetRootRepository;


    public RootRepository(CustomerDAO customerDAO,
                          AccountDAO accountDAO, WalletDAO walletDAO, WalletHistoryDAO walletHistoryDAO,CustomerRootRepository customerRootRepository,
                          AssetRootRepository assetRootRepository){
        this.customerDAO = customerDAO;
        this.walletHistoryDAO = walletHistoryDAO;
        this.accountDAO = accountDAO;
        this.walletDAO = walletDAO;
        this.customerRootRepository = customerRootRepository;
        this.assetRootRepository = assetRootRepository;
    }



    //Methods to construct fully formed objects

    public Customer getCustomerWithWallet(Customer customer) {
        customer = customerRootRepository.getCustomer(customer.getUserId());
        Wallet customerWallet = walletDAO.get(customer.getUserId());
            if (customerWallet.getAssetsInWallet() != null) {
                customerWallet.setAssetsInWallet(assetAbbreviationHelper
                        (customerWallet.getAssetsInWallet()));
                customer.setWallet(customerWallet);
                customerWallet.setCustomer(customer);
            }
        return customer;
    }

    public Customer getCustomerWithAccount(Customer customer){
        Account customerAccount = accountDAO.get(customer.getUserId());
        if (customerAccount == null) {
            accountDAO.create(new Account(customer,BigDecimal.ZERO));
            customerAccount= accountDAO.get(customer.getUserId());
        }
            customer.setAccount(customerAccount);
            customerAccount.setCustomer(customer);
        return customer;
    }

    //need this helper method to prevent corrupting the values in the map, pretty strange but it's the only way I got it to work
    public Map<Asset, BigDecimal> assetAbbreviationHelper(Map<Asset, BigDecimal> incompleteAssetMap) {
        Map<Asset, BigDecimal> completeAssetMap = new HashMap<>();
        for (Map.Entry entry:incompleteAssetMap.entrySet()) {
            Asset asset = (Asset) entry.getKey();
            BigDecimal bigDecimal = (BigDecimal) entry.getValue();
            Asset withAbbreviation = assetRootRepository.getAssetByName(asset.getName()).get();
            completeAssetMap.put(withAbbreviation, bigDecimal);
        }
        return completeAssetMap;
    }

    public WalletHistory getWalletHistoryByCustomer(Customer customer) {
        WalletHistory walletHistory = walletHistoryDAO.get(customer.getUserId());
        walletHistory.setWalletValueHistory(walletHistory.getWalletValueHistory());
        walletHistory.setCustomer(getCustomerWithWallet(customer));
        return walletHistory;
    }

    public void updateWalletHistory(WalletHistory walletHistory) {
        walletHistoryDAO.addCurrentValueToHistory(walletHistory);
    }


    public Optional<Customer> findBuyerOfTransaction(Integer transactionId) {
        Optional<Customer> customerOptional = customerDAO.findBuyerOf(transactionId);
        customerOptional.ifPresent(this::getCustomerWithWallet);
        customerOptional.ifPresent(this::getCustomerWithAccount);
        return customerOptional;
    }

    public Optional<Customer> findSellerOfTransaction(Integer transactionId) {
        Optional<Customer> customerOptional = customerDAO.findSellerOf(transactionId);
        customerOptional.ifPresent(this::getCustomerWithWallet);
        customerOptional.ifPresent(this::getCustomerWithAccount);
        return customerOptional;
    }

    public Optional<Customer> findCustomerOfOrder(Integer orderId) {
        Optional<Customer> customerOptional = customerDAO.findCustomerOfOrder(orderId);
        customerOptional.ifPresent(this::getCustomerWithWallet);
        customerOptional.ifPresent(this::getCustomerWithAccount);
        return customerOptional;
    }

    public Optional<Asset> findAssetOfOrder (Integer orderId) {
        Optional<Asset> assetOptional = assetRootRepository.    findAssetOfOrder(orderId);
       return assetOptional;
    }

}
