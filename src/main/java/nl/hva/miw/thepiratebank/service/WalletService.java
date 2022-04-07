package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.Wallet;
import nl.hva.miw.thepiratebank.domain.WalletHistory;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class WalletService {
    private final RootRepository rootRepository;
    private final AssetRateService assetRateService;

    @Autowired
    public WalletService(RootRepository rootRepository, AssetRateService assetRateService) {
        this.rootRepository = rootRepository;
        this.assetRateService = assetRateService;
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
        Customer customer = rootRepository.getCustomer(userId);
        if (customer == null) {
            return new BigDecimal(0);
        }
        customer = getCustomerWithWallet(customer);
        Asset asset = rootRepository.getAssetByName(assetName).get();
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
        rootRepository.updateWalletAfterTransaction(userId, assetName, newAmount);
    }

    public void creditAssetsInWallet (Customer buyer, String assetName, BigDecimal amount) {
        int userId = buyer.getUserId();
        Wallet buyerWallet = rootRepository.getCustomerWithWallet(buyer).getWallet();
        if (buyerWallet.getAssetsInWallet().containsKey(rootRepository.getAssetByName(assetName).get())){
            BigDecimal newAmount = getAmountOfSingleAssetInWallet(userId, assetName).add(amount);
            rootRepository.updateWalletAfterTransaction(userId, assetName, newAmount);
        } else {
            rootRepository.addToWallet(userId, assetName, amount);
        }
    }
}
