package nl.hva.miw.thepiratebank.controller;

import nl.hva.miw.thepiratebank.config.AdminConfig;
import nl.hva.miw.thepiratebank.domain.Account;
import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.User;
import nl.hva.miw.thepiratebank.domain.Wallet;
import nl.hva.miw.thepiratebank.domain.transfer.*;
import nl.hva.miw.thepiratebank.service.*;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
public class AdminConfigController {
     private AccountService accountService;
     private WalletService walletService;
     private AdminService adminService;
     private AccessTokenService accessTokenService;
     private UserService userService;
     private CustomerService customerService;

     @Autowired
     public AdminConfigController(AccountService accountService, WalletService walletService,
                                  AdminService adminService, AccessTokenService accessTokenService,
                                  UserService userService, CustomerService customerService){
         this.accountService = accountService;
         this.walletService = walletService;
         this.adminService = adminService;
         this.accessTokenService = accessTokenService;
         this.userService = userService;
         this.customerService = customerService;
     }

    @GetMapping("admin/transactionfee")
    public ResponseEntity<?> getTransactionFee(){
        //AdminConfig.setTRANSACTIONFEE(adminService.getTransactionFee());
        BigDecimal transactionFee = AdminConfig.TRANSACTIONFEE;
         if(transactionFee==null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(AdminConfig.TRANSACTIONFEE);
    }

    @PutMapping("admin/newtransactionfee")
    public ResponseEntity<?> setTransactionFee(@RequestBody BigDecimal newTransactionFee) {
        if (newTransactionFee == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Geen transactionFee ontvangen.");
        }
        else if(newTransactionFee.compareTo(BigDecimal.ZERO) <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transactionfee mag niet negatief zijn.");
        }
        AdminConfig.setTRANSACTIONFEE(newTransactionFee);
        adminService.setTansActionFee(newTransactionFee);
        return ResponseEntity.status(HttpStatus.OK).body("Transactionfee gewijzig in: " + AdminConfig.TRANSACTIONFEE);
   }

    @GetMapping("admin/userdata/{userId}")
    @ResponseBody
    public ResponseEntity<?> getUserDataById(@PathVariable int userId){
        User user = userService.getUserByUserId(userId);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Er is geen gebruiker met deze id");
        }
        else{
        BigDecimal transactionFee = adminService.getTransactionFee();
        Account account = accountService.getAccountById(user.getUserId());
        Customer customer = walletService.getCustomerWithWallet(account.getCustomer());
        return ResponseEntity.ok().body(new AdminDataDTO(transactionFee,customer,account));
        }
    }

    @GetMapping("admin/userdataname/{userName}")
    @ResponseBody
    public ResponseEntity<?> getUserDataByUserName(@PathVariable String userName){
        User user = userService.getUserByUserName(userName);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Er is geen gebruiker met deze id");
        }
        else{
            BigDecimal transactionFee = adminService.getTransactionFee();
            Account account = accountService.getAccountById(user.getUserId());
            Customer customer = walletService.getCustomerWithWallet(account.getCustomer());
            return ResponseEntity.ok().body(new AdminDataDTO(transactionFee,customer,account));
        }
    }

    @PutMapping("admin/updatebalance")
    @ResponseBody
    public ResponseEntity<?> setNewAccountValue(@RequestBody AdminAccountBalanceDTO adminAccountBalanceDTO){
         if (adminAccountBalanceDTO==null){
           return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Geen account gekregen");
       }
       else if(adminAccountBalanceDTO.getBalance().compareTo(BigDecimal.ZERO)<0){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account balance mag niet negatief zijn.");
       }
         User user = userService.getUserByUserName(adminAccountBalanceDTO.getUserName());
         Account toChangeAccount = accountService.getAccountById(user.getUserId());
         Customer customer = walletService.getCustomerWithWallet(toChangeAccount.getCustomer());
         Account newAccount = new Account(customer,adminAccountBalanceDTO.getBalance());
         adminService.updateAccount(newAccount);
       return ResponseEntity.ok().body("Account balance gewijzigd in :" + adminAccountBalanceDTO.getBalance());
    }

    @PutMapping("admin/updateassetamount")
    @ResponseBody
    public ResponseEntity<?> setNewAssetValue(@RequestBody AdminAssetAmountDTO adminAssetAmountDTO){
        if (adminAssetAmountDTO==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Geen asset gekregen");
        }
        else if(adminAssetAmountDTO.getAssetAmount().compareTo(BigDecimal.ZERO)<0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Asset hoeveelheid mag niet negatief zijn.");
        }
        User user = userService.getUserByUserName(adminAssetAmountDTO.getUserName());
        int userId = user.getUserId();
        adminService.updateAssetByUserId(userId,adminAssetAmountDTO.getAssetName(),adminAssetAmountDTO.getAssetAmount());
        return ResponseEntity.ok().body("Voor asset: "  + adminAssetAmountDTO.getAssetName() + " bij user: "+ adminAssetAmountDTO.getUserName() +" hoeveelheid gewijzigd naar: " + adminAssetAmountDTO.getAssetAmount());
    }

    @GetMapping(value="admin/assets/{userId}")
    @ResponseBody
    public ResponseEntity<WalletDTO> getAssetsInWallet(@PathVariable int userId){
        Customer customer = customerService.getCustomerById(userId);
        Customer customerWithWallet = walletService.getCustomerWithWallet(customer);
        if (customerWithWallet.getWallet() != null)
            return ResponseEntity.ok().body(new WalletDTO(customerWithWallet.getWallet()));
        return ResponseEntity.ok().body(new WalletDTO());
    }
}
