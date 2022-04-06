package nl.hva.miw.thepiratebank.repository.wallethistory;

import nl.hva.miw.thepiratebank.controller.AssetConsumerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class WalletHistoryDAOTest {
    WalletHistoryDAO walletHistoryDAO;

    //Necessary to prevent postconstruct annotation from running do not delete
    @MockBean
    private AssetConsumerController assetConsumerController;


    @Autowired
    public WalletHistoryDAOTest(WalletHistoryDAO walletHistoryDAO) {
        this.walletHistoryDAO = walletHistoryDAO;
    }

    @Test
    void get() {

    }
}