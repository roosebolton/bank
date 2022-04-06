package nl.hva.miw.thepiratebank.domain.transfer;

import nl.hva.miw.thepiratebank.domain.Asset;
import nl.hva.miw.thepiratebank.service.market.MarketOrderBook;
import nl.hva.miw.thepiratebank.domain.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderBookSumDTO {
    public String assetname;
    public BigDecimal lastprice;
    public Map<BigDecimal,BigDecimal> sellOrderListSum;
    public Map<BigDecimal,BigDecimal> buyOrderListSum;

   private OrderBookSumDTO(OrderbookBuilder builder){
        assetname = builder.assetName;
        sellOrderListSum = builder.sellOrderListSum;
        buyOrderListSum = builder.buyOrderListSum;
        lastprice = builder.lastPrice;
    }

    public static class OrderbookBuilder {
        private BigDecimal lastPrice;
        private String assetName;
        private Map<BigDecimal, BigDecimal> sellOrderListSum;
        private Map<BigDecimal, BigDecimal> buyOrderListSum;

        public OrderbookBuilder() {
            sellOrderListSum = new TreeMap<>();
            buyOrderListSum = new TreeMap<>();
        }

        public OrderBookSumDTO build(MarketOrderBook<Asset> orderBook) {
            this.assetName = orderBook.getAsset().getName();

            List<Order> sellOrders = orderBook.getSellOrders();
            for (Order order : sellOrders) {
                this.sellOrderListSum.merge(order.getLimitPrice(), order.getAmount(), BigDecimal::add);
            }

            List<Order> buyOrders = orderBook.getBuyorders();
            for (Order order : buyOrders) {
                this.buyOrderListSum.merge(order.getLimitPrice(), order.getAmount(), BigDecimal::add);
            }

            this.lastPrice = (buyOrderListSum.keySet().stream().findFirst().orElse(BigDecimal.ZERO)
                    .add(sellOrderListSum.keySet().stream().findFirst().orElse(BigDecimal.ZERO))).divide(BigDecimal.valueOf(2));
            return new OrderBookSumDTO(this);
        }
    }
}
