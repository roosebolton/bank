package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.Wallet;
import nl.hva.miw.thepiratebank.domain.WalletHistory;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.AssetRootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.CustomerRootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.WalletRootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class WalletService {
    private final WalletRootRepository walletRootRepository;
    private final AssetRateService assetRateService;
    private final CustomerRootRepository customerRootRepository;
    private final RootRepository rootRepository;
    private final AssetRootRepository assetRootRepository;

    @Autowired
    public WalletService(AssetRootRepository assetRootRepository, RootRepository rootRepository, CustomerRootRepository customerRootRepository, AssetRateService assetRateService,  WalletRootRepository walletRootRepository) {
        this.customerRootRepository = customerRootRepository;
        this.assetRateService = assetRateService;
        this.assetRootRepository = assetRootRepository;
        this.rootRepository = rootRepository;
        this.walletRootRepository = walletRootRepository;
    }

    public Customer getCustomerWithWallet(Customer customer) {
        customer = rootRepository.getCustomerWithWallet(customer);
        customer.getWallet().setTotalValue(calculateWalletValue
                (customer.getWallet().getAssetsInWallet()));
        return customer;
    }

    public BigDecimal calculateWalletValue(Map<Asset, BigDecimal> walletContent) {
        BigDecimal sum = new BigDecimal(0);
        for (Map.Entry entry: walletContent.entrySet()) {
            Asset asset = (Asset) entry.getKey();
            BigDecimal amount = (BigDecimal) entry.getValue();
            BigDecimal latest = assetRateService.getClosest(asset.getName());
            BigDecimal resultOfAddition = sum.add(latest.multiply(amount));
            sum = resultOfAddition;
        }
        return sum;
    }

    public BigDecimal getAmountOfSingleAssetInWallet(int userId, String assetName) {
        Customer customer = customerRootRepository.getCustomer(userId);
        if (customer == null) {
            return new BigDecimal(0);
        }
        customer = getCustomerWithWallet(customer);
        Asset asset = assetRootRepository.getAssetByName(assetName).get();
        if (customer.getWallet().getAssetsInWallet().get(asset) == null) {
            return new BigDecimal(0);
        }
        return customer.getWallet().getAssetsInWallet().get(asset);
    }
        
    public WalletHistory getWalletHistoryByCustomer(Customer customer) {
        WalletHistory walletHistory = rootRepository.getWalletHistoryByCustomer(customer);
       //logging the total value of the wallet on demand
        walletHistory.getCustomer().getWallet().setTotalValue
                (calculateWalletValue
                        (walletHistory.getCustomer().getWallet().getAssetsInWallet()));
        rootRepository.updateWalletHistory(walletHistory);
        return walletHistory;
    }

    public void debitAssetsInWallet (Customer seller, String assetName, BigDecimal amount){
        int userId = seller.getUserId();
        BigDecimal newAmount = getAmountOfSingleAssetInWallet(userId, assetName).subtract(amount);
        walletRootRepository.updateWalletAfterTransaction(userId, assetName, newAmount);
    }

    public void creditAssetsInWallet (Customer buyer, String assetName, BigDecimal amount) {
        int userId = buyer.getUserId();
        Wallet buyerWallet = rootRepository.getCustomerWithWallet(buyer).getWallet();
        if (buyerWallet.getAssetsInWallet().containsKey(assetRootRepository.getAssetByName(assetName).get())){
            BigDecimal newAmount = getAmountOfSingleAssetInWallet(userId, assetName).add(amount);
            walletRootRepository.updateWalletAfterTransaction(userId, assetName, newAmount);
        } else {
            walletRootRepository.addToWallet(userId, assetName, amount);
        }
    }
}
