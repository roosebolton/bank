package nl.hva.miw.thepiratebank.service.market;

public interface MarketObserver {
    void handleExecute(MarketOrderBook orderBook);
}
