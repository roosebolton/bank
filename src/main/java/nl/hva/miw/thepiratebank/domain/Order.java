package nl.hva.miw.thepiratebank.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Objects;

public class Order implements Comparable<Order> {
    private int orderId;

    private boolean buy;
    private Customer user;
    private Asset asset;
    private BigDecimal amount;
    private BigDecimal limitPrice;
    private Timestamp timeOrderPlaced;
    // price 0 is market order anything else is trigger transaction

    public Order(boolean buy, Customer user, Asset asset, BigDecimal amount, BigDecimal limitPrice) {
        this.buy = buy;
        this.user = user;
        this.asset = asset;
        this.amount = amount;
        this.limitPrice = limitPrice;
        this.timeOrderPlaced = new Timestamp(System.currentTimeMillis());
    }

    public Order(int orderId, boolean buy, BigDecimal amount, BigDecimal limitPrice, Timestamp timeOrderPlaced) {
        this.orderId = orderId;
        this.buy = buy;
        this.user = null;
        this.asset = null;
        this.amount = amount;
        this.limitPrice = limitPrice;
        this.timeOrderPlaced = timeOrderPlaced;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public Customer getUser() {
        return user;
    }

    public void setUser(Customer user) {
        this.user = user;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
    }

    public Timestamp getTimeOrderPlaced() {
        return timeOrderPlaced;
    }

    public void setTimeOrderPlaced(Timestamp timeOrderPlaced) {
        this.timeOrderPlaced = timeOrderPlaced;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return buy == order.buy && Objects.equals(user, order.user) && Objects.equals(asset, order.asset) && Objects.equals(amount, order.amount) && Objects.equals(limitPrice, order.limitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buy, user, asset, amount, limitPrice, timeOrderPlaced);
    }

    @Override
    public int compareTo(Order o) {
        return Comparator.comparing(Order::getLimitPrice)
                .thenComparing(Order::getTimeOrderPlaced)
                .compare(this,o);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", buy=" + buy +
                ", customer=" + user +
                ", asset=" + asset +
                ", amount=" + amount +
                ", price=" + limitPrice +
                ", timeOrderPlaced=" + timeOrderPlaced +
                '}';
    }
}
