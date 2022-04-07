package nl.hva.miw.thepiratebank.repository;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@SpringBootTest
class WalletDAOTest {

    WalletDAO walletDAO;
    CustomerDAO customerDAO;

    @Autowired
    public WalletDAOTest(WalletDAO walletDAO, CustomerDAO customerDAO) {
        this.walletDAO = walletDAO;
        this.customerDAO = customerDAO;
    }
}

