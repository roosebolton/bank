package nl.hva.miw.thepiratebank.service;

import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.WalletHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class WalletServiceTest {

    WalletService walletService;


    @Autowired
    public WalletServiceTest(WalletService walletService) {
        this.walletService = walletService;
    }

    @Test
    void getFullCustomerWallet() {
        Customer customer = new Customer();
        customer.setUserId(1);
        Customer customerWithWallet = walletService.getCustomerWithWallet(customer);
//        System.out.println(customerWithWallet);
    }

    @Test
    void getWalletHistory() {
        Customer customer = new Customer();
        customer.setUserId(1);
        customer = walletService.getCustomerWithWallet(customer);
        WalletHistory walletHistory = walletService.getWalletHistoryByCustomer(customer);
        System.out.println(walletHistory);
    }


    @Test
    void getAssetAmountById() {

    }
}