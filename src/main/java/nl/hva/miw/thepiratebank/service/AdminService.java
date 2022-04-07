package nl.hva.miw.thepiratebank.service;


import nl.hva.miw.thepiratebank.config.AdminConfig;
import nl.hva.miw.thepiratebank.domain.Account;
import nl.hva.miw.thepiratebank.domain.transfer.AdminAssetAmountDTO;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.AccountRootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.ConfigDataRootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.WalletRootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
public class AdminService {
    private final ConfigDataRootRepository configDataRootRepository;
    private final AccountRootRepository accountRootRepository;
    private final WalletRootRepository walletRootRepository;

    @Autowired
    AdminService(ConfigDataRootRepository configDataRootRepository,AccountRootRepository accountRootRepository, WalletRootRepository walletRootRepository){
        this.configDataRootRepository = configDataRootRepository;
        this.accountRootRepository = accountRootRepository;
        this.walletRootRepository = walletRootRepository;
    }

    /**
     * Gets the transactionsFee from Adminconfig.
     */
    public BigDecimal getTransactionFee(){
        return configDataRootRepository.getTransactionFee();
    }

    /**
     * Sets the transactionFee in DB and Adminconfig
     */
    public void setTansActionFee(BigDecimal newTransactionFee){
        configDataRootRepository.setTransactionFee(newTransactionFee);
        configDataRootRepository.setTransactionFee(configDataRootRepository.getTransactionFee());
    }

    /**
     * Sets the transactionFee in DB and Adminconfig
     */
    public void setBankStartingCapital(BigDecimal newBankStartingCapital){
        configDataRootRepository.setBankStartingCapital(newBankStartingCapital);
        configDataRootRepository.setTransactionFee(configDataRootRepository.getTransactionFee());
    }

    /**
     * Gets the transactionsFee from Adminconfig.
     */
    public BigDecimal getBankStartingCaptial(){
        return configDataRootRepository.getBankStartingCapital();
    }

    /**
     * Sets the transactionFee in DB and Adminconfig
     */
    public void setBankId(int newBankID){
        configDataRootRepository.setBankId(newBankID);
        configDataRootRepository.setTransactionFee(configDataRootRepository.getTransactionFee());
    }
    /**
     * Gets the transactionsFee from Adminconfig.
     */
    public int getBankId(){
        return configDataRootRepository.getBankId();
    }

    /// TODO check maken en dao for admin

    public boolean isAdmin () {

        return true;
    }


    public void updateAccount(Account account){
        accountRootRepository.updateAccount(account);
    }

    public void updateAssetByUserId(int userId, String assetName, BigDecimal amount){
        walletRootRepository.updateWalletAfterTransaction(userId,assetName,amount);
    }
}
