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
    public WalletDAOTest(WalletDAO walletDAO, CustomerDAO customerDAO){
        this.walletDAO = walletDAO;
        this.customerDAO = customerDAO;
    }

    @Test
    void get() {
//        System.out.println(walletDAO.get(26));
    }

    @Test
    void update() {
//        Map<Asset, BigDecimal> map = new HashMap<>();
//        map.put(new Asset("bitcoin", "BTC"), new BigDecimal(3));
//        map.put(new Asset("ethereum", "ETH"), new BigDecimal(-8));
//        map.put(new Asset("algorand", "ETH"), new BigDecimal(8));
//        map.put(new Asset("dogecoin"), new BigDecimal(10));
//        Wallet wallet = new Wallet(customerDAO.get(1), map);
//        walletDAO.update(wallet);
    }
}