package nl.hva.miw.thepiratebank.service.market;

import nl.hva.miw.thepiratebank.config.AssetList;
import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.domain.Order;
import nl.hva.miw.thepiratebank.repository.RootRepository;
import nl.hva.miw.thepiratebank.repository.TradeRepository;
import nl.hva.miw.thepiratebank.domain.transfer.OrderBookSumDTO;
import nl.hva.miw.thepiratebank.utilities.exceptions.ConflictException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketService {
    private final RootRepository rootRepository;
    private List<MarketOrderBook<Asset>> orderBooks;
    private final Auctioneer auctioneer;
    private final TradeRepository tradeRepository;

    public MarketService(RootRepository rootReposit, Auctioneer auction, TradeRepository tradeRepo) {
        this.auctioneer = auction;
        this.tradeRepository = tradeRepo;
        this.orderBooks = new ArrayList<>();
        this.rootRepository = rootReposit;
        makeOrderBooks();
        addObserverToOrderBooks();
        loadMarketData();
    }

    public void makeOrderBooks() {
        this.orderBooks = rootRepository.getAllAssets().stream().map(asset -> new MarketOrderBook<>(asset)).collect(Collectors.toList());
    }

    public void matchOrderToOrderBook ( Order order) {
        MarketOrderBook matchedOrderbook = orderBooks.stream()
                .filter(e -> e.getAsset().equals(order.getAsset()))
                .findFirst()
                .orElseThrow(()->new ConflictException("Orderbook of "+order.getAsset()+"not found"));
        matchedOrderbook.addOrder(order);
    }

    public void removeOrder(Order order) {
        MarketOrderBook matchedOrderbook = orderBooks.stream()
                .filter(e -> e.getAsset().equals(order.getAsset())).findFirst().orElseThrow(()->new ConflictException("Orderbook of "+order +"not found"));;
        if(order.isBuy()) {
            matchedOrderbook.getBuyorders().remove(order);
        } else {
            matchedOrderbook.getSellOrders().remove(order);
        }
    }

    public void loadMarketData () {
        this.tradeRepository.findAllOrders().stream().forEach(e -> matchOrderToOrderBook(e));
    }

    public void addObserverToOrderBooks() {
        orderBooks.stream().forEach(e -> e.addMarketObserver(auctioneer));
    }


    public List<MarketOrderBook<Asset>> getOrderAllBooks() {
        return orderBooks;
    }

    public List<MarketOrderBook<Asset>> getOrderBooks(List<String> ids) {
        if(ids.contains("all")) {
            return getOrderAllBooks();
        } else {
            List<MarketOrderBook<Asset>> requestedOrderbook = new ArrayList<>();
            for (String id:ids) {
                orderBooks.stream().filter(e -> e.getAsset().getName().equals(id)).forEach(e -> requestedOrderbook.add(e));
            }
        return requestedOrderbook;
        }

    }

    public MarketOrderBook<Asset> getOrderBook(Asset asset) {
        return orderBooks.stream().filter(e -> e.getAsset().getName().equals(asset.getName())).findFirst().
                orElseThrow(()->new ConflictException("Orderbook of "+ asset.getName()+" not found"));
    }

    public OrderBookSumDTO getOrderBookSum (String asset) {
        if(!AssetList.AVAILABLE_ASSET_COINS_MAP.containsKey(asset)) {
            return null;
        }
        MarketOrderBook orderBook= getOrderBook(rootRepository.getAssetByName(asset)
                .orElseThrow(()-> new ConflictException("Orderbook of " + asset +" not found.")));
        OrderBookSumDTO sumOrderbook= new OrderBookSumDTO.OrderbookBuilder().build(orderBook);
        return sumOrderbook;
    }

}

