package nl.hva.miw.thepiratebank.controller;

import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.WalletHistory;
import nl.hva.miw.thepiratebank.domain.transfer.WalletDTO;
import nl.hva.miw.thepiratebank.domain.transfer.WalletHistoryDTO;
import nl.hva.miw.thepiratebank.service.AdminService;
import nl.hva.miw.thepiratebank.service.CustomerService;
import nl.hva.miw.thepiratebank.service.WalletService;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/portefeuille")
public class WalletController {
    private final WalletService walletService;
    private final CustomerService customerService;
    private final AccessTokenService accessTokenService;
    private final AdminService adminService;

    @Autowired
    public WalletController(WalletService walletService, CustomerService customerService,
                            AccessTokenService accessTokenService, AdminService adminService) {
        this.walletService = walletService;
        this.customerService = customerService;
        this.accessTokenService = accessTokenService;
        this.adminService = adminService;
    }

    @GetMapping(value="/assets")
    public ResponseEntity<WalletDTO> getAssetsInWallet(HttpServletRequest request){
        String strippedToken = request.getHeader("Authorization").replace("Bearer ", "");
        int customerId = Integer.parseInt(accessTokenService.getUserIdFromToken(strippedToken));
        Customer customerWithWallet = walletService.getCustomerWithWallet
                (customerService.getCustomerById(customerId));
        if (customerWithWallet.getWallet() != null)
            return ResponseEntity.ok().body(new WalletDTO(customerWithWallet.getWallet()));
        return ResponseEntity.ok().body(new WalletDTO());
    }

    @GetMapping (value = "/history")
    public ResponseEntity<WalletHistoryDTO> getWalletValueHistory(HttpServletRequest request) {
        String strippedToken = request.getHeader("Authorization").replace("Bearer ", "");
        int customerId = Integer.parseInt(accessTokenService.getUserIdFromToken(strippedToken));
        Customer customer = customerService.getCustomerById(customerId);
        WalletHistory walletHistory = walletService.getWalletHistoryByCustomer(customer);
        if (walletHistory.getWalletValueHistory() != null){
            return ResponseEntity.ok().body(new WalletHistoryDTO(customer.getUserId(), walletHistory.getWalletValueHistory()));}
        return ResponseEntity.ok(new WalletHistoryDTO());
    }

    @GetMapping (value="/bankassets")
    public ResponseEntity<WalletDTO> getBankAssetsInWallet(){
        Customer customerWithWallet = walletService.getCustomerWithWallet
                (customerService.getCustomerById(adminService.getBankId()));
        if (customerWithWallet.getWallet() != null)
            return ResponseEntity.ok().body(new WalletDTO(customerWithWallet.getWallet()));
        return ResponseEntity.ok().body(new WalletDTO());
    }


}

