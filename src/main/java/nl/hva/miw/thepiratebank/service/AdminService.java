package nl.hva.miw.thepiratebank.service;


import nl.hva.miw.thepiratebank.config.AdminConfig;
import nl.hva.miw.thepiratebank.domain.Account;
import nl.hva.miw.thepiratebank.domain.transfer.AdminAssetAmountDTO;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
public class AdminService {
    private RootRepository rootRepository;

    @Autowired
    AdminService(RootRepository rootRepository){
        this.rootRepository = rootRepository;
    }

    /**
     * Gets the transactionsFee from Adminconfig.
     */
    public BigDecimal getTransactionFee(){
        return rootRepository.getTransactionFee();
    }

    /**
     * Sets the transactionFee in DB and Adminconfig
     */
    public void setTansActionFee(BigDecimal newTransactionFee){
        rootRepository.setTransactionFee(newTransactionFee);
        rootRepository.setTransactionFee(rootRepository.getTransactionFee());
    }

    /**
     * Sets the transactionFee in DB and Adminconfig
     */
    public void setBankStartingCapital(BigDecimal newBankStartingCapital){
        rootRepository.setBankStartingCapital(newBankStartingCapital);
        rootRepository.setTransactionFee(rootRepository.getTransactionFee());
    }

    /**
     * Gets the transactionsFee from Adminconfig.
     */
    public BigDecimal getBankStartingCaptial(){
        return rootRepository.getBankStartingCapital();
    }

    /**
     * Sets the transactionFee in DB and Adminconfig
     */
    public void setBankId(int newBankID){
        rootRepository.setBankId(newBankID);
        rootRepository.setTransactionFee(rootRepository.getTransactionFee());
    }
    /**
     * Gets the transactionsFee from Adminconfig.
     */
    public int getBankId(){
        return rootRepository.getBankId();
    }

    /// TODO check maken en dao for admin

    public boolean isAdmin () {

        return true;
    }


    public void updateAccount(Account account){
        rootRepository.updateAccount(account);
    }

    public void updateAssetByUserId(int userId, String assetName, BigDecimal amount){
        rootRepository.updateWalletAfterTransaction(userId,assetName,amount);
    }
}
