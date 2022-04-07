package nl.hva.miw.thepiratebank.service.market;

import nl.hva.miw.thepiratebank.domain.Order;
import nl.hva.miw.thepiratebank.domain.Transaction;
import nl.hva.miw.thepiratebank.repository.TradeRepository;
import nl.hva.miw.thepiratebank.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Component
public class Auctioneer implements MarketObserver{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    BlockingQueue<MarketOrderBook> marketOrderBookBlockingQueue = new ArrayBlockingQueue<>(1000);

    List<Order> buyorders ;
    List<Order> sellOrders;
   private final TradeRepository tradeRepository;
   private final TransactionService transactionService;

    public Auctioneer(TradeRepository tradeRepository, TransactionService transactionService) {
        this.tradeRepository = tradeRepository;
        this.transactionService = transactionService;
    }

    @Override
    public void handleExecute(MarketOrderBook marketOrderBook) {
        marketOrderBookBlockingQueue.add(marketOrderBook);
        try {
            runQueue();
        } catch (InterruptedException exception) {
            log.debug(exception.getMessage());
        }
    }

    public void runQueue() throws InterruptedException {
        MarketOrderBook marketOrderBook = marketOrderBookBlockingQueue.take();
        this.buyorders = marketOrderBook.getBuyorders();
        this.sellOrders = marketOrderBook.getSellOrders();
        sortOrderList(marketOrderBook);
        matchOrders();
    }


    public void sortOrderList (MarketOrderBook marketOrderBook) {
        Collections.sort(marketOrderBook.getBuyorders());
        Collections.reverse(marketOrderBook.getBuyorders());
        Collections.sort(marketOrderBook.getSellOrders());
    }

    public List<Transaction> matchOrders () {
        List<Transaction> filledTransaction = new ArrayList<>();
        if (buyorders.stream().findFirst().isPresent()) {
            // gebruikt hier iterator, omdat er wat uit originele
            Iterator<Order> it = buyorders.iterator();
            while (it.hasNext()) {
                Order order = it.next();
                Boolean buyorderfulfilled = compareOrder(order);
                if(buyorderfulfilled) {
                    it.remove();
                }
            }
            // als buy order price, vergelijkbaar of kleiner is dan sell price
        }
        return filledTransaction;
    }

    public boolean compareOrder (Order order) {
        List<Order> filteredSell = sellOrders.stream().filter(e -> e.getUser().getUserId()!=order.getUser().getUserId()).collect(Collectors.toList());
        // als er geen sell order zijn
        if (!filteredSell.stream().findFirst().isPresent()) {
            return false;
        }

        // check if order is still valid ()
        Order buyOrderOriginal = order;
        Order sellOrderOriginal = filteredSell.stream().findFirst().get();
        if ( order.getLimitPrice().compareTo(sellOrderOriginal.getLimitPrice()) >= 0) {
            // dan hoeveelheid checken,als buyorder amount groter is dan sellorder amount
            BigDecimal amountFilled =  order.getAmount().min(sellOrderOriginal.getAmount());
            BigDecimal difference = order.getAmount().subtract(sellOrderOriginal.getAmount());
            Transaction matchedTransaction = transactionService.buildTransaction(buyOrderOriginal, sellOrderOriginal, amountFilled);
            // check if orders is still valid

            if (!transactionService.hasSufficientBalance(matchedTransaction)) {
                return false;
            }
            if(!transactionService.hasSufficientNumberofAssets(matchedTransaction)) {
               sellOrders.remove(sellOrderOriginal);
                return false;
            }

            if(!transactionService.isValidTransaction(matchedTransaction)){
                return false;
            }
            transactionService.saveTransaction(matchedTransaction);

            if (difference.signum()>0) {
                    sellOrders.remove(0);
                    removeOrderFromDB(sellOrderOriginal);
                    order.setAmount(buyOrderOriginal.getAmount().subtract(amountFilled));
                    update(order);
            }
            if (difference.signum()<0) {
                    sellOrders.get(0).setAmount(sellOrderOriginal.getAmount().subtract(amountFilled));
                    removeOrderFromDB(order);
                    update(sellOrders.get(0));
                    return true;
            }
            if (difference.signum()==0) {
                    sellOrders.remove(0);
                    removeOrderFromDB(sellOrderOriginal);
                    removeOrderFromDB(order);
                    return true;
            }
        }
        return false;
    }

    public void removeOrderFromDB(Order order) {
        tradeRepository.deleteOrder(order.getOrderId());
    }

    public void update(Order order) {
        tradeRepository.updateOrder(order);
    }



}
