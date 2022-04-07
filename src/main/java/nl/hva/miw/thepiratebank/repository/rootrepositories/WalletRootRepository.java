package nl.hva.miw.thepiratebank.repository.rootrepositories;

import nl.hva.miw.thepiratebank.repository.WalletDAO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class WalletRootRepository {

    WalletDAO walletDAO;

    public WalletRootRepository(WalletDAO walletDAO) {
        this.walletDAO = walletDAO;
    }

    //Wallet

    public void updateWalletAfterTransaction(int userId, String assetName, BigDecimal amount) {
        walletDAO.updateWalletAfterTransaction(userId, assetName, amount);
    }

    public void addToWallet(int userId, String assetName, BigDecimal amount){walletDAO.addSingleAssetToWallet(userId, assetName, amount);}

}
