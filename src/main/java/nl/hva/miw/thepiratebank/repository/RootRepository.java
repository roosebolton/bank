package nl.hva.miw.thepiratebank.repository;


import nl.hva.miw.thepiratebank.domain.*;

import nl.hva.miw.thepiratebank.repository.assetrate.AssetRateDAO;
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
    private final UserDAO userDAO;
    private final AssetDAO assetDAO;
    private final WalletHistoryDAO walletHistoryDAO;
    private final AccountDAO accountDAO;
    private final WalletDAO walletDAO;
    private final AssetRateDAO assetRateDAO;
    private final ConfigDataDAO configDataDAO;

    public RootRepository(CustomerDAO customerDAO, UserDAO userDAO, AssetDAO assetDAO,
                          AccountDAO accountDAO, WalletDAO walletDAO, WalletHistoryDAO walletHistoryDAO, AssetRateDAO assetRateDAO,
    ConfigDataDAO configDataDAO){
        this.customerDAO = customerDAO;
        this.userDAO = userDAO;
        this.assetDAO = assetDAO;
        this.walletHistoryDAO = walletHistoryDAO;
        this.accountDAO = accountDAO;
        this.walletDAO = walletDAO;
        this.assetRateDAO = assetRateDAO;
        this.configDataDAO = configDataDAO;
    }

//Customers

    /**
     * Gets a list with all customers
     * @return A list with all customers
     */
    public List<Customer> getAllCustomers(){
        return customerDAO.getAll();
    }

    /**
     * Creates a customer, given a Customer object
     * @param customer
     */
    public void createCustomer(Customer customer){
        customerDAO.create(customer);
    }

    /**
     * Gets a customer, based on the id.
     * @param id
     * @return Customer by id if exists, null otherwise
     */
    public Customer getCustomer(Integer id){
        return customerDAO.get(id);
    }

    /**
     * Updates an existing client with the information of a given client
     * @param customer
     */
    public void updateCustomer(Customer customer){
        customerDAO.update(customer);
    }

    /**
     * Deletes an existing client, if found by its id
     * @param id
     */
    public void deleteCustomer(Integer id){
        customerDAO.delete(id);
    }

    ///Users

    /**
     * Get a list of all users
     * @return A list of all users
     */
    public List<User> getAllUsers(){
        return userDAO.getAll();
    }

    /**
     * Creates a new user, based on a given user
     * @param user
     */
    public void createUser(User user){
        userDAO.create(user);
    }

    /**
     * Gets an existing user by its given id
     * @param id
     * @return An existing user by id, null if userid does not exist
     */
    public User getUser(Integer id){
        return userDAO.get(id);
    }

    /**
     * Updates an existing user based on a given user
     * @param user
     */
    public void updateUser(User user){
       userDAO.update(user);
    }

    /**
     * Deletes an existing user, based on its id
     * @param id
     */
    public void deleteUser(Integer id){
       userDAO.delete(id);
    }

    public  User getByUserName(String username) {
        return userDAO.getByUsername(username);
    }

    //Asset
    public Optional<Asset> getAssetByName(String assetname) {
        Asset asset = assetDAO.get(assetname);
        return Optional.ofNullable(asset);
    }


    public Optional<Asset> getAssetWithFullHistory (String assetname) {
        Asset asset = assetDAO.get(assetname);
        Optional<Asset> assetOptional = Optional.ofNullable(asset);
        List<AssetRate> assetRatelist = assetRateDAO.getFullHistory(assetname);
        assetOptional.ifPresent(element -> element.setPricehistory(assetRatelist));
                return assetOptional;
    }

    public Optional<Asset> findAssetOfTransaction(Integer transactionid) {
       Optional<Asset>  assetOptional = assetDAO.findAssetOfTransaction(transactionid);
          return assetOptional;

    }

    public List<Asset> getAllAssets() {
        return assetDAO.getAll();
    }

    //Assetrate
    public AssetRate insertAssetByName (AssetRate assetRate) {
        Asset asset = assetDAO.get(assetRate.getAsset().getName());
        assetRate.setAsset(asset);
        return assetRate;
    }


    //Account
    public Account getAccountById(int id){
        return accountDAO.get(id);
    }


    public void updateAccount(Account account) {
        accountDAO.update(account);
    }

    public void createAccount(Account acount){accountDAO.create(acount);}

    //Wallet

    public void updateWalletAfterTransaction(int userId, String assetName, BigDecimal amount) {
        walletDAO.updateWalletAfterTransaction(userId, assetName, amount);
    }

    public void addToWallet(int userId, String assetName, BigDecimal amount){walletDAO.addSingleAssetToWallet(userId, assetName, amount);}

    //Methods to construct fully formed objects

    public Customer getCustomerWithWallet(Customer customer) {
        customer = getCustomer(customer.getUserId());
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
            Asset withAbbreviation = getAssetByName(asset.getName()).get();
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
        Optional<Asset> assetOptional = assetDAO.findAssetOfOrder(orderId);
       return assetOptional;
    }




    //ConfigData
    public BigDecimal getTransactionFee(){
        return configDataDAO.getTransActionFee();
    }

    public void setTransactionFee(BigDecimal newTransactionFee){
        configDataDAO.setTransactionFee(newTransactionFee);
    }

    public BigDecimal getBankStartingCapital(){
        return configDataDAO.getBankStartingCapital();
    }

    public void setBankStartingCapital(BigDecimal newBankStartingCapital){
        configDataDAO.setBankStartingCapital(newBankStartingCapital);
    }

    public int getBankId(){
        return configDataDAO.getBankId();
    }

    public void setBankId(int newBankId){
        configDataDAO.setBankId(newBankId);
    }





}
