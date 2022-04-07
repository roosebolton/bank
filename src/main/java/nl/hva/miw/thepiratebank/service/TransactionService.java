package nl.hva.miw.thepiratebank.service;

import com.google.gson.JsonArray;
import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Customer;
import nl.hva.miw.thepiratebank.domain.Transaction;
import nl.hva.miw.thepiratebank.domain.transfer.TransactionDTO;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.TradeRepository;
import nl.hva.miw.thepiratebank.domain.Order;

import nl.hva.miw.thepiratebank.repository.rootrepositories.AssetRootRepository;
import nl.hva.miw.thepiratebank.repository.rootrepositories.CustomerRootRepository;
import nl.hva.miw.thepiratebank.utilities.authorization.token.AccessTokenService;
import nl.hva.miw.thepiratebank.utilities.exceptions.AuthorizationException;
import nl.hva.miw.thepiratebank.utilities.exceptions.InvalidJsonInputException;
import nl.hva.miw.thepiratebank.utilities.exceptions.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TradeRepository tradeRepository;
    private final AccessTokenService accessTokenService;
    private final RootRepository rootRepository;
    private final WalletService walletService;
    private final AccountService accountService;
    private final AdminService adminService;
    private final AssetRateService assetRateService;
    private final AssetRootRepository assetRootRepository;
    private final CustomerRootRepository customerRootRepository;

    @Autowired
    public TransactionService(CustomerRootRepository customerRootRepository, AssetRootRepository assetRootRepository, TradeRepository tradeRepository, AccessTokenService accessTokenService, RootRepository rootRepository, WalletService walletService,
                              AccountService accountService, AdminService adminService, AssetRateService assetRateService) {
        this.tradeRepository = tradeRepository;
        this.accessTokenService = accessTokenService;
        this.rootRepository = rootRepository;
        this.walletService = walletService;
        this.accountService = accountService;
        this.adminService = adminService;
        this.assetRateService = assetRateService;
        this.assetRootRepository = assetRootRepository;
        this.customerRootRepository = customerRootRepository;
    }

    public boolean isValidErrorMessage(Transaction transaction) {
        return transactionError(transaction).isEmpty();
    }

    public JsonArray transactionError(Transaction transaction) {
        JsonArray errorArray = new JsonArray();
        if (!isDifferentAccount(transaction)) {
            errorArray.add("De rekening van de koper en verkoper moeten verschillen");
        }
        if (!hasSufficientBalance(transaction)) {
            errorArray.add( "Het saldo van de geldrekening is onvoldoende voor deze transactie");
        }
        if (!hasSufficientNumberofAssets(transaction)) {
          errorArray.add( "Er is onvoldoende " + transaction.getAsset().getName() + " voor deze transactie");
        }
        return errorArray;
    }

    public boolean isValidTransaction  (Transaction transaction) {
       return (isDifferentAccount(transaction) && hasSufficientBalance(transaction) && hasSufficientNumberofAssets(transaction));
    }

    public boolean isDifferentAccount(Transaction transaction) {
        return !(transaction.getBuyer().getUserId() == transaction.getSeller().getUserId());
    }

    public boolean hasSufficientBalance(Transaction transaction) {
        Customer buyer = rootRepository.getCustomerWithAccount(transaction.getBuyer());
        return (transaction.getValue().add(transaction.getTransactionCost()).compareTo(buyer.getAccount().getBalance()) <= 0);
    }

    public boolean hasSufficientNumberofAssets(Transaction transaction) {
        BigDecimal assetInWallet = walletService.getAmountOfSingleAssetInWallet(transaction.getSeller().getUserId(),
                transaction.getAsset().getName());
        return (assetInWallet.compareTo(transaction.getAssetAmount()) >= 0);
    }

    public void saveTransaction(Transaction transaction) {
        tradeRepository.saveTransaction(transaction);
        walletService.debitAssetsInWallet(transaction.getSeller(), transaction.getAsset().getName(), transaction.getAssetAmount());
        walletService.creditAssetsInWallet(transaction.getBuyer(), transaction.getAsset().getName(), transaction.getAssetAmount());
        accountService.doDebitAndCreditBalance(transaction);
    }

    public Transaction buildTransaction(TransactionDTO transactionDTO) {
        if(!transactionDTO.isNonNull()){
            throw new InvalidJsonInputException();

        }
        Asset asset = assetRootRepository.getAssetByName(transactionDTO.getAssetname()).orElseThrow(()->new ConflictException("Asset of transaction not found."));

        Transaction transaction = new Transaction.TransactionBuilder()
                .buyer(customerRootRepository.getCustomer(transactionDTO.getBuyer()))
                .seller(customerRootRepository.getCustomer(transactionDTO.getSeller()))
                .asset(asset)
                .assetAmount(transactionDTO.getAssetamount())
                .value(assetRateService.getCurrentValue(asset.getName()).multiply(transactionDTO.getAssetamount()))
                .transactionCost(adminService.getTransactionFee())
                .transactionDate(new Timestamp(System.currentTimeMillis()))
                .build();

        BigDecimal transactionCostCalculated = transaction.getTransactionCost().multiply(transaction.getValue());
        transaction.setTransactionCost(transactionCostCalculated);
        return transaction;
    }

    public Transaction buildTransaction (Order buyorder, Order sellorder, BigDecimal amountFilled) {
       Transaction transaction = new Transaction.TransactionBuilder()
                .buyer(buyorder.getUser())
                .seller(sellorder.getUser())
                .asset(buyorder.getAsset())
                .assetAmount(amountFilled)
                .value(sellorder.getLimitPrice().multiply(amountFilled))
                .transactionCost((sellorder.getLimitPrice().multiply(amountFilled)).multiply(adminService.getTransactionFee()))
                .transactionDate(new Timestamp(System.currentTimeMillis()))
                .build();
        return transaction;
    }

    public Transaction findTransactionById(Integer id)  {
        return tradeRepository.findTransactionById(id)
                .orElseThrow(() ->new ConflictException("Transaction not found."));
    }

    public List<Transaction> getTransactionHistory(int userId) {
        List<Transaction> transactionHistory = getBuyTransactionsByUserId(userId);
        transactionHistory.addAll(getSellTransactionsByUserId(userId));
        return transactionHistory.stream().sorted().collect(Collectors.toList());
    }

    public List<Transaction> getLimitedTransactionHistory(int userId) {
        List<Transaction> transactionHistory = getBuyTransactionsByUserId(userId);
        transactionHistory.addAll(getSellTransactionsByUserId(userId));
        Collections.reverse(transactionHistory);
        return transactionHistory.stream().limit(5).collect(Collectors.toList());
    }

    public List<Transaction> getBuyTransactionsByUserId(Integer userId) {
        return tradeRepository.findBuyTransactionsByUserId(userId);
    }

    public List<Transaction> getSellTransactionsByUserId(Integer userId) {
        return tradeRepository.findSellTransactionsByUserId(userId);
    }

    public List<Transaction> getAllTransaction () {
        return tradeRepository.findAllTransactions();
    }

    public boolean verifyAuthorizationTransaction(TransactionDTO transactionDTO, HttpServletRequest request) {
         int userId = accessTokenService.getTokenFromRequest(request).orElseThrow(() -> new AuthorizationException("Authorization of user failed"));
           return ((transactionDTO.getBuyer() == 0 && transactionDTO.getSeller() == userId)
                    || (transactionDTO.getBuyer() == userId && transactionDTO.getSeller() == 0));
    }

}
