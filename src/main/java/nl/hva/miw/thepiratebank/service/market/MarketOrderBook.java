package nl.hva.miw.thepiratebank.service.market;


import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.hva.miw.thepiratebank.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


public class MarketOrderBook<Asset> {
    @JsonIgnore
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final List<MarketObserver> marketObservers;

    private Asset asset;
    private List<Order> sellOrders;
    private List<Order> buyorders;

    public MarketOrderBook(Asset asset) {
        this.marketObservers = new ArrayList<>();
        this.sellOrders = new ArrayList<>();
        this.buyorders = new ArrayList<>();
        this.asset = asset;
    }

    public void addOrder (Order order) {
        if (order.isBuy()) {buyorders.add(order);
        } else {
            sellOrders.add(order);}

        System.out.println(this);
        System.out.println("sellorders"+sellOrders);
        notifyMarketObserver(this);
    }

    public void addMarketObserver ( MarketObserver marketObserver) {
        marketObservers.add(marketObserver);
    }

    private void notifyMarketObserver(MarketOrderBook orderBook) {
        marketObservers.forEach( e -> e.handleExecute(orderBook));
    }

    private void removeBuyOrder (Order order) {
        buyorders.remove(order);
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public List<Order> getSellOrders() {
        return sellOrders;
    }

    public void setSellOrders(List<Order> sellOrders) {
        this.sellOrders = sellOrders;
    }

    public List<Order> getBuyorders() {
        return buyorders;
    }

    public void setBuyorders(List<Order> buyorders) {
        this.buyorders = buyorders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketOrderBook<?> that = (MarketOrderBook<?>) o;
        return Objects.equals(asset, that.asset) && Objects.equals(sellOrders, that.sellOrders) && Objects.equals(buyorders, that.buyorders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asset, sellOrders, buyorders);
    }

    @Override
    public String toString() {
        return "MarketOrderBook{" +
                "asset=" + asset +
                ", sellOrders=" + sellOrders.toString() +
                ", buyorders=" + buyorders.toString() +
                '}';
    }
}
