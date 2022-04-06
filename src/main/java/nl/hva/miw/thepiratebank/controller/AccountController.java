package nl.hva.miw.thepiratebank.controller;

import nl.hva.miw.thepiratebank.domain.Account;
import nl.hva.miw.thepiratebank.domain.transfer.AccountDTO;
import nl.hva.miw.thepiratebank.service.AccountService;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AccountController {

    private AccountService accountService;
    private AccessTokenService accessTokenService;

    @Autowired
    public AccountController(AccountService accountService, AccessTokenService accessTokenService) {
        this.accountService = accountService;
        this.accessTokenService = accessTokenService;
    }

    @GetMapping("/account")
    public ResponseEntity<?> getAccount(HttpServletRequest request){
        String strippedToken = request.getHeader("Authorization").replace("Bearer ", "");
        int customerId = Integer.parseInt(accessTokenService.getUserIdFromToken(strippedToken));
        Account account = accountService.getAccountById(customerId);
        if (account == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(new AccountDTO(account));
    }


}