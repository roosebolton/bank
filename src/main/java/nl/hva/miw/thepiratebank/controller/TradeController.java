package nl.hva.miw.thepiratebank.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import nl.hva.miw.thepiratebank.config.AdminConfig;
import nl.hva.miw.thepiratebank.domain.Transaction;
import nl.hva.miw.thepiratebank.domain.transfer.TransactionDTO;
import nl.hva.miw.thepiratebank.service.TransactionService;

import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import nl.hva.miw.thepiratebank.utilities.exceptions.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class TradeController {
    private final TransactionService transactionService;
    private final AccessTokenService accessTokenService;

    @Autowired
    public TradeController(TransactionService transactionService, AccessTokenService accessTokenService) {
        super();
        this.transactionService = transactionService;
        this.accessTokenService = accessTokenService;
    }

    @PostMapping(value = "/trade")
    public ResponseEntity<?> tradeAssets(@RequestBody TransactionDTO transactionDTO, HttpServletRequest request) {
        Gson gson = new Gson();
        try {
            transactionService.verifyAuthorizationTransaction(transactionDTO,request);
            Transaction transaction = transactionService.buildTransaction(transactionDTO);
            if (transactionService.isValidErrorMessage(transaction)) {
                transactionService.saveTransaction(transaction);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Transactie geslaagd.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(gson.toJson(transactionService.transactionError(transaction)));
            }
        } catch (NullPointerException nullPointerException) {
            JsonObject response = new JsonObject();
            response.addProperty("Error","Onbekende fout, probeer later nog een keer");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(response));
        }
    }

    @GetMapping(value = "/trade/history")
    public ResponseEntity<List> getTradeHistoryById(HttpServletRequest request) {
        List<Transaction> transactionHistory = transactionService.getLimitedTransactionHistory(accessTokenService.getTokenFromRequest(request)
                .orElseThrow(() ->new ConflictException("Error reading token for user")));
        return ResponseEntity.ok().body(transactionHistory);
    }

    @GetMapping(value ="/trade/transactionfee")
    public ResponseEntity<?> getTransactionFee(){
        BigDecimal transactionFee = AdminConfig.TRANSACTIONFEE;
        if(transactionFee==null){
             throw new ConflictException("No Transactionfee found");
        }
        return ResponseEntity.ok().body(AdminConfig.TRANSACTIONFEE);
    }

}
